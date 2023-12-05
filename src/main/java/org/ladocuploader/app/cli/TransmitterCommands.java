package org.ladocuploader.app.cli;

import com.google.common.collect.Lists;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import formflow.library.data.Submission;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.ladocuploader.app.data.Transmission;
import org.ladocuploader.app.data.TransmissionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipOutputStream;

@Slf4j
@ShellComponent
public class TransmitterCommands {

    private final TransmissionRepository transmissionRepository;

    private final SftpClient sftpClient;

    public TransmitterCommands(TransmissionRepository transmissionRepository,
                               SftpClient sftpClient) {
        this.transmissionRepository = transmissionRepository;
        this.sftpClient = sftpClient;
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
        Map<String, Submission> transmissionIdToSubmission = new HashMap<>();

        allSubmissionIds.forEach(id -> {
            Transmission transmission = transmissionRepository.getTransmissionBySubmission(Submission.builder().id(id).build());
            Submission submission = transmission.getSubmission();
            // TODO: this might not be necessary - because this is a 1:1 relationship
            transmissionIdToSubmission.put(transmission.getTransmission_id().toString(), submission);
        });

        log.info("Preparing batches");
        // partition into csvs with limit of size 1000
        var transmissionIdBatches = Lists.partition(transmissionIdToSubmission.keySet().stream().toList(), 1000);
        var batch_index = 0;
        for (var transmissionIdBatch : transmissionIdBatches) {
            Map<String, Submission> transmissionIdToSubmissionBatch = new HashMap<>();
            for (var transmissionId : transmissionIdBatch) {
                transmissionIdToSubmissionBatch.put(transmissionId, transmissionIdToSubmission.get(transmissionId));
            }

            log.info("Starting batch of size={}", transmissionIdToSubmissionBatch.size());
            transmitBatch(transmissionIdToSubmissionBatch, batch_index);
            batch_index ++;
        }
    }

    private void transmitBatch(Map<String, Submission> appIdToSubmissionBatch, Integer batchIndex) throws IOException, JSchException, SftpException{
        String zipFilename = createZipFilename(batchIndex);
        List<UUID> successfullySubmittedIds = zipFiles(appIdToSubmissionBatch, zipFilename);

        // send zip file
        log.info("Uploading zip file");
        sftpClient.uploadFile(zipFilename);

        // Update transmission in DB
        successfullySubmittedIds.forEach(id -> {
            Submission submission = Submission.builder().id(id).build();
            Transmission transmission = transmissionRepository.getTransmissionBySubmissionAndType(submission, "ECE");
            transmission.setTimeSent(new Date());
            transmission.setStatus("success");
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

    private List<UUID> zipFiles(Map<String, Submission> transmissionIdToSubmission, String zipFileName) throws IOException {
        List<UUID> successfullySubmittedIds = new ArrayList<>();
        try (FileOutputStream baos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            Map<String, Object> csvData = generateCsvs(transmissionIdToSubmission);
            // loop over failed records and mark as failed
            Map<UUID, String> failedSubmissions = (Map<UUID, String>) csvData.get("failedRecords");

             // TODO: receive the list of streams and package into a ZipFile

//            Map<String, Object> csvData = csvService.

//            for (var transmissionAndSubmission : transmissionIdToSubmission.entrySet()) {
//                var transmissionId = transmissionAndSubmission.getKey();
//                var submission = transmissionAndSubmission.getValue();
//
//                Transmission transmission = transmissionRepository.getTransmissionBySubmission(submission);
//                if (transmission == null) {
//                    log.error("Missing transmission for submission {}", submission.getId());
//                    continue;
//                }
//
//                // TODO: do we need this check?
//                if (!isComplete(submission)) {
////                    transmission.setLastTransmissionFailureReason("skip_incomplete");
//                    transmissionRepository.save(transmission);
//                    continue;
//                }
//
//                if ("laDigitalAssister".equals(submission.getFlow())) {
//                    // TODO: do we need a subfolder?
////                    String subfolder = createSubfolderName(submission, transmission);
//                    try {
//                            // generate applicant summary
////                            byte[] file = renderPDFOrCrash(submission);
////                            String fileName = "00_" + pdfService.generatePdfName(submission);
////                            if (!fileName.endsWith(".pdf")) {
////                                fileName += ".pdf";
////                            }
//                            // TODO: generate all CSV files - or use dummy files for now
//
////                            String fileName = submission.getId()
//
//                            byte[] file = new byte[0];
//
////                            zos.putNextEntry(new ZipEntry(subfolder));
//                            ZipEntry entry = new ZipEntry(fileName);
//                            entry.setSize(file.length);
//                            zos.putNextEntry(entry);
//                            zos.write(file);
//                            zos.closeEntry();
//
//                        successfullySubmittedIds.add(submission.getId());
//                    } catch (Exception e) {
//                        transmission.setStatus("failed");
//                        transmissionRepository.save(transmission);
//                        log.error("Error generating file collection for submission ID {}", submission.getId(), e);
//                    }
//                } else {
//                    log.info("Skipping - it is part of another flow={}", submission.getFlow());
//                }
//            }
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

