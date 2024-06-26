package org.ladocuploader.app.cli;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import formflow.library.data.Submission;
import formflow.library.data.UserFile;
import formflow.library.data.UserFileRepositoryService;
import formflow.library.file.CloudFile;
import formflow.library.file.CloudFileRepository;
import formflow.library.pdf.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.data.Transmission;
import org.ladocuploader.app.data.TransmissionRepository;
import org.ladocuploader.app.data.enums.TransmissionStatus;
import org.ladocuploader.app.data.enums.TransmissionType;
import org.ladocuploader.app.submission.StringEncryptor;
import org.ladocuploader.app.utils.SubmissionUtilities;
import org.slf4j.MDC;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.ladocuploader.app.file.DocTypeEnum.*;

@Slf4j
@Service
@ComponentScan("org.ladocuploader.app.cli")
public class SubmissionTransfer {

    private static final ZoneId CST = ZoneId.of("America/Chicago");
    private static final Map<String, String> DOCTYPE_FORMAT_MAP = new HashMap<>();

    static {
        DOCTYPE_FORMAT_MAP.put(BIRTH_CERTIFICATE.getValue(), "PD-Birth Verification");
        DOCTYPE_FORMAT_MAP.put(DRIVERS_LICENSE.getValue(), "PD-Identification");
        DOCTYPE_FORMAT_MAP.put(SOCIAL_SECURITY_CARD.getValue(), "PD-SSN");
        DOCTYPE_FORMAT_MAP.put(CHECK_STUB.getValue(), "INC-Wage Documents");
        DOCTYPE_FORMAT_MAP.put(OTHER_INCOME.getValue(), "INC-Other Unearned Income");
        DOCTYPE_FORMAT_MAP.put(BILL.getValue(), "EXP-Utility");
        DOCTYPE_FORMAT_MAP.put(MEDICAL_INFO.getValue(), "MED-Medical Forms");
        DOCTYPE_FORMAT_MAP.put(BANKING_INFO.getValue(), "RES-Bank Statement");
        DOCTYPE_FORMAT_MAP.put(MARRIAGE_LICENSE.getValue(), "LEG-Marriage License");
        DOCTYPE_FORMAT_MAP.put(DIVORCE_DECREE.getValue(), "LEG-Divorce / Separation Decree");
        DOCTYPE_FORMAT_MAP.put(COURT_ORDER.getValue(), "LEG-Court Orders");
        DOCTYPE_FORMAT_MAP.put(PATERNITY.getValue(), "PAT-Acknowledgement of Paternity (ES)");
        DOCTYPE_FORMAT_MAP.put(OTHER.getValue(), "CORR-Customer Correspondence");
    }

    private final int BATCH_INDEX_LEN = "00050000000" .length();
    private final int BATCH_SIZE_LIMIT = 25;
    private final long TWO_HOURS = 2L;
    public static final DateTimeFormatter MMDDYYYY_HHMMSS = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

    private final TransmissionRepository transmissionRepository;
    private final UserFileRepositoryService fileRepositoryService;
    private final CloudFileRepository fileRepository;
    private final PdfService pdfService;
    private final PGPEncryptor snapPgpEncryptor;

    private final StringEncryptor encryptor;
    private final FtpsClient ftpsClient;


    public SubmissionTransfer(TransmissionRepository transmissionRepository, UserFileRepositoryService fileRepositoryService,
            CloudFileRepository fileRepository, PdfService pdfService, PGPEncryptor snapPgpEncryptor, StringEncryptor encryptor,
            FtpsClient ftpsClient) {
        this.transmissionRepository = transmissionRepository;
        this.fileRepositoryService = fileRepositoryService;
        this.fileRepository = fileRepository;
        this.pdfService = pdfService;
        this.snapPgpEncryptor = snapPgpEncryptor;
        this.encryptor = encryptor;
        this.ftpsClient = ftpsClient;
    }

    @Scheduled(fixedRateString = "${transmissions.snap-transmission-rate}")
    public void transferSubmissions() {
        // Give a 2-hour wait for folks to upload documents
        OffsetDateTime submittedAtCutoff = OffsetDateTime.now().minusHours(TWO_HOURS);
        List<Submission> queuedSubmissions = transmissionRepository.submissionsToTransmit(Sort.unsorted(), TransmissionType.SNAP);
        int totalQueued = queuedSubmissions.size();
        if (queuedSubmissions.isEmpty()) {
            log.info("Nothing to transmit. Exiting.");
            return;
        }
        log.info("Found %s queued transmissions" .formatted(totalQueued));

        queuedSubmissions = queuedSubmissions.stream()
                .filter(submission -> (submission.getSubmittedAt().isBefore(submittedAtCutoff))).toList();
        log.info("Excluding %s submitted within last 2 hours" .formatted(totalQueued - queuedSubmissions.size()));
        if (queuedSubmissions.isEmpty()) {
            log.info("No submissions older than 2 hour to transmit. Exiting.");
            return;
        }
        log.info("Found %s transmissions to transmit" .formatted(queuedSubmissions.size()));
        var submissionBatches = Lists.partition(queuedSubmissions, BATCH_SIZE_LIMIT);
        int transmittedCount = 0;
        for (var submissionBatch : submissionBatches) {
            transmittedCount += submissionBatch.size();
            log.info("Transmitting %s/%s" .formatted(transmittedCount, queuedSubmissions.size()));
            transferSubmissionBatch(submissionBatch);
        }
        log.info("Done transmitting batches. Transmitted %s of %s batches. Exiting." .formatted(transmittedCount,
                submissionBatches.size()));
    }

    private void transferSubmissionBatch(List<Submission> submissionsBatch) {
        // Get submissions to transfer
        String batchSeq = Long.toString(transmissionRepository.nextValueBatchSequence());
        String batchIndex = Strings.padStart(batchSeq, BATCH_INDEX_LEN, '0');
        UUID uuid = UUID.randomUUID();
        String zipFileName = batchIndex + ".zip";
        MDC.put("batchIndex", batchIndex);
        MDC.put("run_id", uuid.toString());
        log.info(String.format("Beginning transfer of %s: batch %s", uuid, batchIndex));

        // Stats on transfers
        int failed = 0;
        List<Transmission> successfulTransmissions = new ArrayList<>();
        int subfolderidx = 1;
        try (FileOutputStream baos = new FileOutputStream(zipFileName);
                ZipOutputStream zos = new ZipOutputStream(baos)) {
            StringBuilder docMeta = new StringBuilder();
            for (Submission submission : submissionsBatch) {
                Transmission transmission = transmissionRepository.findBySubmissionAndTransmissionType(submission,
                        TransmissionType.SNAP);
                String subfolder = Integer.toString(subfolderidx++);
                MDC.put("submissionId", submission.getId().toString());
                MDC.put("transmissionId", transmission.getTransmission_id().toString());
                try {
                    log.info("Verifying required fields");
                    verifyRequiredFieldsArePresent(submission.getInputData());

                    log.info("Generate applicant summary");
                    packageSnapApplication(batchIndex, zos, docMeta, submission, subfolder);

                    packageUploadedDocuments(batchIndex, zos, docMeta, submission, subfolder);

                    successfulTransmissions.add(transmission);
                } catch (Exception e) {
                    log.error("Error generating file collection for submission ID {}", submission.getId(), e);

                    transmission.setDocumentationErrors(Map.of("error", e.getMessage(), "subfolder", subfolder));
                    transmission.setStatus(TransmissionStatus.Failed);
                    updateTransmission(uuid, transmission);
                    failed++;
                }
                MDC.remove("submissionId");
                MDC.remove("transmissionId");
            }

            // Add metadata entry to zip
            ZipEntry docEntry = new ZipEntry(batchIndex + ".txt");
            byte[] docMetaData = docMeta.toString().getBytes();
            docEntry.setSize(docMetaData.length);
            zos.putNextEntry(docEntry);
            zos.write(docMetaData);
            zos.closeEntry();
            zos.close();

            // Encrypt and transfer
            byte[] data = snapPgpEncryptor.signAndEncryptPayload(zipFileName);
            ftpsClient.uploadFile(zipFileName + ".gpg", data);
        } catch (Exception ex) {
            log.error("Transfer failed for batch %s, run_id %s" .formatted(batchIndex, uuid), ex);
            transmissionRepository.rollbackBatchSequence(Long.parseLong(batchSeq) - 1);
            return;
        } finally {
            // Cleanup
            if (new File(zipFileName).delete()) {
                log.info("Deleting zip file");
            }
        }

        // Wait until these have successfully transmitted before marking as complete
        for (var transmission : successfulTransmissions) {
            log.info("Updating transmission statuses to Complete");
            transmission.setStatus(TransmissionStatus.Complete);
            updateTransmission(uuid, transmission);
        }

        MDC.put("successful", String.valueOf(successfulTransmissions.size()));
        MDC.put("failed", String.valueOf(failed));
        log.info(String.format("Completed transfer of batch %s, total %s, successful %s, failed %s", batchIndex, subfolderidx - 1,
                successfulTransmissions.size(), failed));
        MDC.clear();
    }

    private void verifyRequiredFieldsArePresent(Map<String, Object> inputData) {
        boolean hasFirstName = inputData.containsKey("firstName");
        boolean hasLastName = inputData.containsKey("lastName");
        boolean hasAddress =
                inputData.containsKey("homeAddressStreetAddress1") || inputData.containsKey("mailingAddressStreetAddress1");
        boolean hasSignature = inputData.containsKey("signature");

        if (!(hasFirstName && hasLastName && hasAddress && hasSignature)) {
            throw new IllegalArgumentException("Required field(s) are missing");
        }
    }

    private void updateTransmission(UUID uuid, Transmission transmission) {
        transmission.setTimeSent(new Date());
        transmission.setRunId(uuid);
        transmissionRepository.save(transmission);
    }

    private void packageUploadedDocuments(String batchIndex, ZipOutputStream zos, StringBuilder docMeta, Submission submission,
            String subfolder) throws IOException {
        List<UserFile> userFiles = fileRepositoryService.findAllBySubmission(submission);
        if (userFiles.isEmpty()) {
            return;
        }
        log.info("Number of uploaded docs to transfer: %s" .formatted(userFiles.size()));
        int fileCount = 0;
        for (UserFile userFile : userFiles) {
            fileCount += 1;
            String docUploadFilename = SubmissionUtilities.createFileNameForUploadedDocument(submission, userFile, fileCount,
                    userFiles.size());
            String docOwnerName = submission.getInputData().get("documentOwner_wildcard_" + userFile.getFileId())
                    .toString().replace(" ", "_");
            ZipEntry docEntry = new ZipEntry(batchIndex + "/" + subfolder + "/" + docUploadFilename);
            docEntry.setSize(userFile.getFilesize().longValue());
            zos.putNextEntry(docEntry);

            CloudFile docFile = fileRepository.get(userFile.getRepositoryPath());
            zos.write(docFile.getFileBytes());
            zos.closeEntry();

            // write doc metadata
            String docType = getDocType(submission, userFile);
            List<String> documentOwnerWholeName = formatHouseholdMemberName(submission.getInputData(), docOwnerName);
            String documentOwnerFirstName = documentOwnerWholeName.get(0);
            String documentOwnerLastName = documentOwnerWholeName.get(1);
            String metaEntry = generateMetaDataEntry(batchIndex, subfolder, docUploadFilename, docType, submission,
                    documentOwnerFirstName, documentOwnerLastName);
            docMeta.append(metaEntry);
        }
    }

    private static String getDocType(Submission submission, UserFile userFile) {
        var docType = (String) submission.getInputData().get("docType_wildcard_" + userFile.getFileId());
        return DOCTYPE_FORMAT_MAP.getOrDefault(docType, "CORR-Customer Correspondence");
    }

    private void packageSnapApplication(String batchIndex, ZipOutputStream zos, StringBuilder docMeta, Submission submission,
            String subfolder) throws IOException {
        byte[] file = pdfService.getFilledOutPDF(submission); // TODO Handle generate-pdf crashes
        String fileName = "SNAP_application.pdf";
        zos.putNextEntry(new ZipEntry(batchIndex + "/" + subfolder + "/"));
        ZipEntry entry = new ZipEntry(batchIndex + "/" + subfolder + "/" + fileName);
        entry.setSize(file.length);
        zos.putNextEntry(entry);
        zos.write(file);
        zos.closeEntry();

        String metaEntry = generateMetaDataEntry(batchIndex, subfolder, fileName, "APP-OFS 4 APP", submission,
                submission.getInputData().getOrDefault("firstName", "").toString(),
                submission.getInputData().getOrDefault("lastName", "").toString());
        docMeta.append(metaEntry);
    }

    private String generateMetaDataEntry(String batchIndex,
            String subfolder,
            String filename,
            String documentType,
            Submission submission,
            String documentOwnerFirstName,
            String documentOwnerLastName) {
        Map<String, Object> inputData = submission.getInputData();
        String formattedSSN = formatSSN(inputData);
        String formattedFilename = removeFileExtension(filename);
        String formattedBirthdate = formatBirthdate(submission.getInputData(), formattedFilename);
        ZonedDateTime submittedAt = submission.getSubmittedAt().atZoneSameInstant(ZoneId.systemDefault());
        String formattedSubmissionDate = submittedAt.withZoneSameInstant(CST).format(MMDDYYYY_HHMMSS);
        String fileLocation = String.format("%s/%s/%s", batchIndex, subfolder, filename);

        return "\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n"
                .formatted(
                        batchIndex,
                        formattedFilename,
                        documentType,
                        documentOwnerFirstName,
                        documentOwnerLastName,
                        formattedSSN,
                        formattedBirthdate,
                        formattedSubmissionDate,
                        fileLocation);
    }

    private String formatSSN(Map<String, Object> inputData) {
        String encryptedSSN = encryptor.decrypt((String) inputData.getOrDefault("encryptedSSN", ""));
        return encryptedSSN.replace("-", "");
    }

    private static String removeFileExtension(String filename) {
        int extIdx = filename.indexOf('.');
        if (extIdx != -1) {
            return filename.substring(0, extIdx);
        }
        return filename;
    }

    private static List<String> formatHouseholdMemberName(Map<String, Object> inputData, String formattedFilename) {
        List<String> householdMemberName = new ArrayList<>();

        if (formattedFilename.startsWith(
                inputData.get("firstName").toString().replace(" ", "_") + "_" + inputData.get("lastName").toString()
                        .replace(" ", "_"))) {
            householdMemberName.add((String) inputData.get("firstName"));
            householdMemberName.add((String) inputData.get("lastName"));
            return householdMemberName;
        }

        if (inputData.containsKey("household")) {
            List<Map<String, Object>> household = (List<Map<String, Object>>) inputData.get("household");
            if (!household.isEmpty()) {
                Optional<Map<String, Object>> member = household.stream()
                        .filter(hhm -> formattedFilename.startsWith(
                                hhm.get("householdMemberFirstName").toString().replace(" ", "_") + "_" + hhm.get(
                                        "householdMemberLastName").toString().replace(" ", "_")))
                        .findFirst();
                if (member.isPresent()) {
                    householdMemberName.add((String) member.get().get("householdMemberFirstName"));
                    householdMemberName.add((String) member.get().get("householdMemberLastName"));
                    return householdMemberName;
                }
            }
        }
        return householdMemberName;
    }

    private static String formatBirthdate(Map<String, Object> inputData, String formattedFilename) {
        String day;
        String month;
        String year;
        if (inputData.containsKey("household")) {
            List<Map<String, Object>> household = (List<Map<String, Object>>) inputData.get("household");
            if (!household.isEmpty()) {
                Optional<Map<String, Object>> member = household.stream()
                        .filter(hhm -> formattedFilename.startsWith(
                                hhm.get("householdMemberFirstName").toString().replace(" ", "_") + "_" + hhm.get(
                                        "householdMemberLastName").toString().replace(" ", "_")))
                        .findFirst();
                if (member.isPresent()) {
                    day = (String) member.get().getOrDefault("householdMemberBirthDay", "");
                    month = (String) member.get().getOrDefault("householdMemberBirthMonth", "");
                    year = (String) member.get().getOrDefault("householdMemberBirthYear", "");
                    return "%s/%s/%s" .formatted(Strings.padStart(day, 2, '0'), Strings.padStart(month, 2, '0'), year);
                }
            }
        }

        day = (String) inputData.getOrDefault("birthDay", "");
        month = (String) inputData.getOrDefault("birthMonth", "");
        year = (String) inputData.getOrDefault("birthYear", "");

        return "%s/%s/%s" .formatted(Strings.padStart(day, 2, '0'), Strings.padStart(month, 2, '0'), year);
    }
}
