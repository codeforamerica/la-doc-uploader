package org.ladocuploader.app.cli;

import com.google.common.collect.Lists;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;
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
import org.springframework.data.domain.Sort;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@ShellComponent
public class TransmitterCommands {

    private final TransmissionRepository transmissionRepository;

    private final SftpClient sftpClient;

    private final CsvService csvService;

    public TransmitterCommands(TransmissionRepository transmissionRepository,
                               SftpClient sftpClient, CsvService csvService) {
        this.transmissionRepository = transmissionRepository;
        this.sftpClient = sftpClient;
        this.csvService = csvService;
    }

    @ShellMethod(key = "transmit")
    public void transmit() throws IOException, JSchException, SftpException {
        log.info("Finding submissions to transmit...");
        var allSubmissions = this.transmissionRepository.submissionsToTransmit(Sort.unsorted());
        log.info("Total submissions to transmit in all batches is {}", allSubmissions.size());

        List<UUID> allSubmissionIds = new ArrayList<>();
        allSubmissions.forEach(submission -> {
            allSubmissionIds.add(submission.getId());
        });

        log.info("Preparing batches");
        // partition into csvs with limit of size 500
        var submissionIdBatches = Lists.partition(allSubmissionIds, 500);
        var batch_index = 0;
        for (var submissionIdBatch : submissionIdBatches) {
            Map<UUID, Submission> submissionIdToSubmissionBatch = new HashMap<>();
            for (var submissionId : submissionIdBatch) {
                Submission submission = Submission.builder().id(submissionId).build();
                submissionIdToSubmissionBatch.put(submissionId, submission);
            }

            log.info("Starting batch of size={}", submissionIdToSubmissionBatch.size());
            transmitBatch(submissionIdToSubmissionBatch, batch_index);
            batch_index ++;
        }
    }

    private void transmitBatch(Map<UUID, Submission> submissionIdToSubmission, Integer batchIndex) throws IOException, JSchException, SftpException{
        String zipFilename = createZipFilename(batchIndex);
        List<UUID> successfullySubmittedIds = zipFiles(submissionIdToSubmission, zipFilename);

        // send zip file
        log.info("Uploading zip file");
        sftpClient.uploadFile(zipFilename);

        // Update transmission in DB
        successfullySubmittedIds.forEach(id -> {
            Submission submission = Submission.builder().id(id).build();
            Transmission transmission = transmissionRepository.findBySubmissionAndTransmissionType(submission, TransmissionType.ECE);
            transmission.setTimeSent(new Date());
            transmission.setStatus(TransmissionStatus.Complete);
            transmissionRepository.save(transmission);
        });
        log.info("Finished transmission of a batch");
    }

    @NotNull
    private static String createZipFilename(Integer batchIndex) {
        // Format: Apps__2023-07-05__0.zip
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

        return "Apps__" + date + "__" + batchIndex + ".zip";
    }

    private Map<String, Object> generateCsvs(Map<String, Submission> transmissionIdToSubmission) {
        Map<String, Object> csvData = new HashMap<>(Map.ofEntries(
                Map.entry("ParentGuardian", new byte[0]),
                Map.entry("Relationships", new byte[0]),
                Map.entry("Student", new byte[0]),
                Map.entry("failedRecords", new HashMap<UUID, String>())
        ));
        for (var submission: transmissionIdToSubmission.values()){
            // randomly add failed records
            if (Math.random() > 0.5){
                csvData.put("failedRecords", Map.entry(submission.getId(), "Failed"));
            }
        }

        return csvData;

    }

    private List<UUID> zipFiles(Map<UUID, Submission> submissionIdToSubmission, String zipFileName) throws IOException {
        List<UUID> successfullySubmittedIds = new ArrayList<>();
        try (FileOutputStream baos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            CsvPackage ecePackage = csvService.generateCsvPackage(submissionIdToSubmission.values().stream().toList(), CsvPackageType.ECE_PACKAGE);
            // TODO: collect failures
            byte [] studentDoc = ecePackage.getCsvDocument(CsvType.STUDENT).getCsvData();
            byte [] parentGuardianDoc = ecePackage.getCsvDocument(CsvType.PARENT_GUARDIAN).getCsvData();
            ZipEntry entry = new ZipEntry("student.csv");
            entry.setSize(studentDoc.length);
            zos.putNextEntry(entry);
            zos.write(studentDoc);
            zos.closeEntry();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }

        return successfullySubmittedIds;
    }

    private static boolean isComplete(Submission submission) {
        // TODO: add filter to find complete submissions for laDigitalAssisterFlow
        // Bail if the submission looks incomplete
//        var inputData = submission.getInputData();
//        if (submission.getFlow().equals("pebt")) {
//            if (inputData.get("hasMoreThanOneStudent") == null || inputData.get("firstName") == null || inputData.get("signature") == null) {
//                log.warn("Declining to transmit incomplete pebt app submissionId={} -- hasMoreThanOneStudent or firstName or signature is missing", submission.getId());
//                return false;
//            }
//        } else if (submission.getFlow().equals("docUpload")) {
//            if (inputData.get("firstName") == null || inputData.get("lastName") == null || inputData.get("applicationNumber") == null) {
//                log.warn("Declining to transmit incomplete doc upload submissionId={} -- firstName or lastName or applicationNumber is missing", submission.getId());
//                return false;
//            }
//        }
        return true;
    }

    private static boolean doTransmitApplication(String appNumber, Submission submission) {
        // TODO: use this to delay submission if it is less than 2 hours old to prevent doc-less submissions?
        // delay 7 days if there aren't any uploaded docs associated with this application
//        Instant submittedAt = submission.getSubmittedAt().toInstant();
//        long diffDays = ChronoUnit.DAYS.between(submittedAt, Instant.now());
//        boolean submitted7daysAgo = Math.abs(diffDays) >= 7;
//        List<String> missingDocUploads = SubmissionUtilities.getMissingDocUploads(submission);
//        boolean hasUploadedDocs = missingDocUploads.isEmpty();
        return true;
    }

//    @NotNull
//    private static String createSubfolderName(Submission submission, Transmission transmission) {
//        // TODO: eliminate this?
//        Map<String, Object> inputData = submission.getInputData();
//        if ("pebt".equals(submission.getFlow())) {
//            return transmission.getConfirmationNumber() + "_" + inputData.get("lastName") + "/";
//        } else {
//            return "LaterDoc_" + inputData.get("applicationNumber") + "_" + inputData.get("lastName") + "_" + inputData.get("firstName") + "/";
//        }
//    }

}

