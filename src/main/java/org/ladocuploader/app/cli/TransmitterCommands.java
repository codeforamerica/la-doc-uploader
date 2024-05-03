package org.ladocuploader.app.cli;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;
import formflow.library.data.UserFile;
import formflow.library.file.CloudFile;
import formflow.library.file.CloudFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.ladocuploader.app.csv.CsvPackage;
import org.ladocuploader.app.csv.CsvService;
import org.ladocuploader.app.csv.enums.CsvPackageType;
import org.ladocuploader.app.csv.enums.CsvType;
import org.ladocuploader.app.data.Transmission;
import org.ladocuploader.app.data.TransmissionRepository;
import org.ladocuploader.app.data.enums.TransmissionStatus;
import org.ladocuploader.app.data.enums.TransmissionType;
import org.ladocuploader.app.utils.SubmissionUtilities;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@ComponentScan("org.ladocuploader.app.cli")
public class TransmitterCommands {

    private final TransmissionRepository transmissionRepository;

    private final SftpClient sftpClient;

    private final CsvService csvService;

    private final CloudFileRepository fileRepository;

    private final String failedSubmissionKey = "failed";

    private final String failedDocumentationKey = "failed_documentation";

    private final String successfulSubmissionKey = "success";

    private final List<TransmissionType> transmissionTypes = List.of(TransmissionType.ECE, TransmissionType.WIC);

    private final long TWO_HOURS = 2L;

    private final PGPEncryptor wicPgpEncryptor;

    private final PGPEncryptor ecePgpEncryptor;

    public TransmitterCommands(TransmissionRepository transmissionRepository,
                               SftpClient sftpClient, CsvService csvService, CloudFileRepository cloudFileRepository, PGPEncryptor wicPgpEncryptor, PGPEncryptor ecePgpEncryptor) {
        this.transmissionRepository = transmissionRepository;
        this.sftpClient = sftpClient;
        this.csvService = csvService;
        this.fileRepository = cloudFileRepository;
        this.wicPgpEncryptor = wicPgpEncryptor;
        this.ecePgpEncryptor = ecePgpEncryptor;
    }

    @Scheduled(cron="${transmissions.wic-ece-transmission-schedule}")
    public void transmit() throws IOException, JSchException, SftpException {
        log.info("Finding submissions to transmit...");
        OffsetDateTime submittedAtCutoff = OffsetDateTime.now().minusHours(TWO_HOURS);
        for (TransmissionType transmissionType : transmissionTypes) {

            List<Submission> queuedSubmissions = transmissionRepository.submissionsToTransmit(Sort.unsorted(), transmissionType);
            int totalQueued = queuedSubmissions.size();
            if (queuedSubmissions.isEmpty()) {
                log.info("Nothing to transmit. Exiting.");
                return;
            }
            log.info("Found %s queued transmissions".formatted(totalQueued));

            queuedSubmissions = queuedSubmissions.stream()
                    .filter(submission -> (submission.getSubmittedAt().isBefore(submittedAtCutoff))).toList();
            log.info("Total submissions to transmit for {} is {}", transmissionType.name(), queuedSubmissions.size());
            if (queuedSubmissions.size() > 0) {
                log.info("Transmitting submissions for {}", transmissionType.name());
                transmitBatch(queuedSubmissions, transmissionType);
            } else {
                log.info("Skipping transmission for {}", transmissionType.name());
            }

        }

    }

    private void transmitBatch(List<Submission> submissions, TransmissionType transmissionType) throws IOException, JSchException, SftpException{

        UUID runId = UUID.randomUUID();
        String zipFilename = createZipFilename(transmissionType, runId);
        CsvPackageType csvPackageType = transmissionType.getPackageType();
        Map<String, Object> zipResults = zipFiles(submissions, zipFilename, csvPackageType);

        List<UUID> successfullySubmittedIds = (List<UUID>) zipResults.get(successfulSubmissionKey);

        Map<UUID, Map<CsvType, String>> failedSubmissions = (Map<UUID, Map<CsvType, String>>) zipResults.get(failedSubmissionKey);

        Map<UUID, Map<String, String>> failedDocumentation = (Map<UUID, Map<String, String>>) zipResults.get(failedDocumentationKey);

        // send zip file
        String uploadLocation = csvPackageType.getUploadLocation();
        if (csvPackageType.getEncryptPackage()){
            log.info("Encrypting data package");
            byte [] data = new byte[]{};
            if (csvPackageType == CsvPackageType.WIC_PACKAGE) {
                log.info("Uploading encrypted WIC zip file from memory");
                data = wicPgpEncryptor.signAndEncryptPayload(zipFilename);
            } else if (csvPackageType == CsvPackageType.ECE_PACKAGE ){
                log.info("Uploading encrypted ECE zip file from memory");
                data = ecePgpEncryptor.signAndEncryptPayload(zipFilename);
            }

            sftpClient.uploadFile(zipFilename, uploadLocation, data);
        } else {
            log.info("Uploading zip file");
            sftpClient.uploadFile(zipFilename, uploadLocation);
        }

        if (new File(zipFilename).delete()) {
            log.info("Deleting zip file");
        }

        successfullySubmittedIds.forEach(id -> {
            Submission submission = Submission.builder().id(id).build();
            Transmission transmission = transmissionRepository.findBySubmissionAndTransmissionType(submission, transmissionType);
            transmission.setTimeSent(new Date());
            transmission.setStatus(TransmissionStatus.Complete);
            transmission.setRunId(runId);
            transmissionRepository.save(transmission);
        });
        log.info("Finished transmission of a batch");

        failedSubmissions.forEach((id, errorMessages) -> {
                    Submission submission = Submission.builder().id(id).build();
                    Transmission transmission = transmissionRepository.findBySubmissionAndTransmissionType(submission, transmissionType);
                    transmission.setStatus(TransmissionStatus.Failed);
                    transmission.setRunId(runId);
                    transmission.setSubmissionErrors(errorMessages);
                    transmissionRepository.save(transmission);
                }
        );
        failedDocumentation.forEach((id, errorMessages) -> {
                    Submission submission = Submission.builder().id(id).build();
                    Transmission transmission = transmissionRepository.findBySubmissionAndTransmissionType(submission, transmissionType);
                    transmission.setDocumentationErrors(errorMessages);
                    transmissionRepository.save(transmission);
                }
        );



    }

    @NotNull
    private static String createZipFilename(TransmissionType transmissionType, UUID runId) {
        // Format: Apps__<TRANSMISSION_TYPE>__<RUN_ID>__2023-07-05.zip
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        return "Apps__" + transmissionType.name() + "__" + runId + "__" + date + ".zip";
    }

    private void addZipEntries(CsvPackage csvPackage, ZipOutputStream zipOutput){
        CsvPackageType packageType = csvPackage.getPackageType();
        List<CsvType> csvTypes = packageType.getCsvTypeList();
        csvTypes.forEach(csvType ->
                {
                    try {
                        byte[] document = csvPackage.getCsvDocument(csvType).getCsvData();
                        ZipEntry entry = new ZipEntry(csvType.getFileName());
                        entry.setSize(document.length);
                        zipOutput.putNextEntry(entry);
                        zipOutput.write(document);
                        zipOutput.closeEntry();
                    } catch (Exception e) {
                        log.error("Failed to generate csv document %s for package".formatted(csvType));
                    }

                }
        );
    }

    private Map<String, Object> zipFiles(List<Submission> submissions, String zipFileName, CsvPackageType packageType) throws IOException {
        Map<String, Object> results = new HashMap<>();
        List<UUID> successfullySubmittedIds = new ArrayList<>(submissions.stream()
                .map(Submission::getId)
                .toList());

        try (FileOutputStream baos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            CsvPackage csvPackage = csvService.generateCsvPackage(submissions, packageType);
            Map<UUID, Map<CsvType, String>> submissionErrors = csvPackage.getErrorMessages();
            Map<UUID, Map<UUID, String>> documentationErrors = new HashMap<>();

            submissionErrors.forEach((submissionId, submissionErrorMessages) -> {
                        successfullySubmittedIds.remove(submissionId);
                    }
            );

            addZipEntries(csvPackage, zos);
            if (csvPackage.getPackageType().getIncludeDocumentation()) {
                submissions.forEach(submission -> {
                    List<UserFile> userFiles = transmissionRepository.userFilesBySubmission(submission);
                    log.info("Found " + userFiles.size() + " files associated with app.");
                    if (userFiles.size() == 0) {
                        return;
                    }
                    int fileCount = 0;
                    String subfolder = submission.getId() + "_files/";
                    try {
                        zos.putNextEntry(new ZipEntry(subfolder));
                    } catch (Exception e) {
                        log.error("Error generating file collection for submission ID {}", submission.getId(), e);
                    }
                    Map<UUID, String> submissionDocumentationErrors = new HashMap<>();
                    for (UserFile userFile : userFiles) {
                        try {
                            fileCount += 1;
                            String fileName = userFile.getOriginalName();
                            log.info("filename is: " + fileName);
                            String formattedFileName = SubmissionUtilities.createFileNameForUploadedDocument(submission, userFile, fileCount, userFiles.size());
                            ZipEntry docEntry = new ZipEntry(subfolder + formattedFileName);
                            docEntry.setSize(userFile.getFilesize().longValue());
                            zos.putNextEntry(docEntry);
                            CloudFile docFile = fileRepository.get(userFile.getRepositoryPath());
                            byte[] bytes = new byte[Math.toIntExact(docFile.getFileSize())];

                            try (ByteArrayInputStream fis = new ByteArrayInputStream(docFile.getFileBytes())) {
                                fis.read(bytes);
                                zos.write(bytes);
                            }
                            zos.closeEntry();
                            File file = new File(userFile.getRepositoryPath());
                            file.delete(); // delete after download and added to zipfile
                        } catch (Exception e) {
                            submissionDocumentationErrors.put(userFile.getFileId(), e.getMessage());
                            log.error("Error generating file collection for submission ID {}", submission.getId(), e);
                        }
                    }
                    documentationErrors.put(submission.getId(), submissionDocumentationErrors);

                });
            }

            results.put(failedDocumentationKey, documentationErrors);

            results.put(failedSubmissionKey, submissionErrors);

        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }

        results.put(successfulSubmissionKey, successfullySubmittedIds);

        return results;
    }
}

