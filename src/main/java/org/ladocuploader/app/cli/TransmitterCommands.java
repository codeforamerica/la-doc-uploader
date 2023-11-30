package org.ladocuploader.app.cli;

import com.google.common.collect.Lists;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import formflow.library.data.Submission;
import formflow.library.data.UserFile;
import formflow.library.pdf.PdfService;
import formflow.library.pdf.SubmissionFieldPreparer;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.ladocuploader.app.data.Transmission;
import org.ladocuploader.app.data.TransmissionRepository;
import org.ladocuploader.app.utils.SubmissionUtilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.FileInputStream;
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

    public TransmitterCommands(TransmissionRepository transmissionRepository,
                               SftpClient sftpClient,
                               @Value("${form-flow.laterdoc-delay-disabled}") String disableLaterdocDelay) {
        this.transmissionRepository = transmissionRepository;
        this.sftpClient = sftpClient;
    }

    @ShellMethod(key = "transmit")
    public void transmit() throws IOException, JSchException, SftpException {
        log.info("Finding submissions to transmit...");
        var allSubmissions = this.transmissionRepository.submissionsToTransmit(Sort.unsorted());
        log.info("Total submissions to transmit in all batches is {}", allSubmissions.size());

        log.info("Computing which app IDs have later docs");
        List<UUID> allSubmissionIds = new ArrayList<>();
        allSubmissions.forEach(submission -> {
            allSubmissionIds.add(submission.getId());
        });
        Map<String, Submission> appIdToSubmission = new HashMap<>();

        allSubmissionIds.forEach(id -> {
            Transmission transmission = transmissionRepository.getTransmissionBySubmission(Submission.builder().id(id).build());
            Submission submission = transmission.getSubmission();
            appIdToSubmission.put(transmission.getConfirmationNumber(), submission);

        });

        //TODO: Do we need batches? Maybe for documents?

        log.info("Preparing batches");
        var appIdBatches = Lists.partition(appIdToSubmission.keySet().stream().toList(), 200);
        for (var appIdBatch : appIdBatches) {
            Map<String, Submission> appIdToSubmissionBatch = new HashMap<>();
            for (var appId : appIdBatch) {
                appIdToSubmissionBatch.put(appId, appIdToSubmission.get(appId));
            }

            log.info("Starting batch of size={}", appIdToSubmissionBatch.size());
            transmitBatch(appIdToSubmissionBatch);
        }
    }

    private void transmitBatch(Map<String, Submission> appIdToSubmissionBatch) throws IOException, JSchException, SftpException{
        String zipFilename = createZipFilename(appIdToSubmissionBatch);
        List<UUID> successfullySubmittedIds = zipFiles(appIdToSubmissionBatch, zipFilename);

        // send zip file
        log.info("Uploading zip file");
        sftpClient.uploadFile(zipFilename);

        UUID batchId = UUID.randomUUID();

        // Update transmission in DB
        successfullySubmittedIds.forEach(id -> {
            Submission submission = Submission.builder().id(id).build();
            Transmission transmission = transmissionRepository.getTransmissionBySubmission(submission);
            transmission.setTimeSent(new Date());
            transmission.setStatus("success");
            transmission.setBatchId(batchId);
            transmissionRepository.save(transmission);
        });
        log.info("Finished transmission of a batch");
    }

    @NotNull
    private static String createZipFilename(Map<String, Submission> appIdToSubmission) {
        // Format: Apps__2023-07-05.zip
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

//        String firstAppId = appIdToSubmission.keySet().stream().min(String::compareTo).get();
//        String lastAppId = appIdToSubmission.keySet().stream().max(String::compareTo).get();

        return "Apps__" + date + ".zip";
    }

    private List<UUID> zipFiles(Map<String, Submission> appIdToSubmission, String zipFileName) throws IOException {
        List<UUID> successfullySubmittedIds = new ArrayList<>();
        try (FileOutputStream baos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (var appNumberAndSubmission : appIdToSubmission.entrySet()) {
                var appNumber = appNumberAndSubmission.getKey();
                var submission = appNumberAndSubmission.getValue();

                Transmission transmission = transmissionRepository.getTransmissionBySubmission(submission);
                if (transmission == null) {
                    log.error("Missing transmission for submission {}", submission.getId());
                    continue;
                }

                // TODO: is this necessary? Can we just pick up submissions which are ready?
                if (!isComplete(submission)) {
                    transmission.setLastTransmissionFailureReason("skip_incomplete");
                    transmissionRepository.save(transmission);
                    continue;
                }

                if ("laDigitalAssister".equals(submission.getFlow())) {
                    // TODO: do we need a subfolder?
//                    String subfolder = createSubfolderName(submission, transmission);
                    try {
                            // generate applicant summary
//                            byte[] file = renderPDFOrCrash(submission);
//                            String fileName = "00_" + pdfService.generatePdfName(submission);
//                            if (!fileName.endsWith(".pdf")) {
//                                fileName += ".pdf";
//                            }
                            // TODO: generate all CSV files - or use dummy files for now

                            byte[] file = new byte[0];

                            zos.putNextEntry(new ZipEntry(subfolder));
                            ZipEntry entry = new ZipEntry(subfolder + fileName);
                            entry.setSize(file.length);
                            zos.putNextEntry(entry);
                            zos.write(file);
                            zos.closeEntry();

                        successfullySubmittedIds.add(submission.getId());
                    } catch (Exception e) {
                        transmission.setStatus("failed");
                        transmissionRepository.save(transmission);
                        log.error("Error generating file collection for submission ID {}", submission.getId(), e);
                    }
                } else {
                    transmission.setLastTransmissionFailureReason("skip_awaiting_laterdocs");
                    transmissionRepository.save(transmission);
                }
            }
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
        // TODO: eliminate this?
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

