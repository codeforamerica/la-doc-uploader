package org.ladocuploader.app.cli;

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

import java.io.File;
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
        var allSubmissions = this.transmissionRepository.submissionsToTransmit(Sort.unsorted(), TransmissionType.ECE);
        log.info("Total submissions to transmit for ECE is {}", allSubmissions.size());
        if (allSubmissions.size() > 0) {
            log.info("Transmitting submissions for ECE");
            transmitBatch(allSubmissions, TransmissionType.ECE);
        } else {
            log.info("Skipping transmission for ECE");
        }
    }

    private void transmitBatch(List<Submission> submissions, TransmissionType transmissionType) throws IOException, JSchException, SftpException{
        String zipFilename = createZipFilename(transmissionType);
        // TODO: get errors for the packageType
        Map<String, Object> zipResults = zipFiles(submissions, zipFilename);

        List<UUID> successfullySubmittedIds = (List<UUID>) zipResults.get("success");

        Map<UUID, Map<CsvType, String>> failedSubmissions = (Map<UUID, Map<CsvType, String>>) zipResults.get("failed");

        // send zip file
        log.info("Uploading zip file");
        String uploadLocation = transmissionType.getPackageType().getUploadLocation();
        sftpClient.uploadFile(zipFilename, uploadLocation);
        File zip = new File(zipFilename);
        if (zip.delete()) {
            log.info("Deleted the folder: " + zip.getName());
        } else {
            log.info("Failed to delete the folder " + zip.getName());
        }

        UUID runId = UUID.randomUUID();

        // Update transmission in DB for success
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



    }

    @NotNull
    private static String createZipFilename(TransmissionType transmissionType) {
        // Format: Apps__<TRANSMISSION_TYPE>__2023-07-05.zip
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        return "Apps__" + transmissionType.name() + "__" + date + ".zip";
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
                        // TODO: add errors to the error map if something fails with zip?
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
        );
    }

    private Map<String, Object> zipFiles(List<Submission> submissions, String zipFileName) throws IOException {
        Map<String, Object> results = new HashMap<>();
        List<UUID> successfullySubmittedIds = new ArrayList<>(submissions.stream()
                .map(Submission::getId)
                .toList());

        try (FileOutputStream baos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            CsvPackage ecePackage = csvService.generateCsvPackage(submissions, CsvPackageType.ECE_PACKAGE);
            Map<UUID, Map<CsvType, String>> submissionErrors = ecePackage.getErrorMessages();
            log.info(submissionErrors.toString());

            submissionErrors.forEach((submissionId, submissionErrorMessages) -> {
                    successfullySubmittedIds.remove(submissionId);
                    }
            );

            // TODO: at what point do we not submit the package/ mark as failed? does marking as failed mean that we should retry?

            addZipEntries(ecePackage, zos);
            results.put("failed", submissionErrors);

        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }

        results.put("success", successfullySubmittedIds);

        return results;
    }

}

