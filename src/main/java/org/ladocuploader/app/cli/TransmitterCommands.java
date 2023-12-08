package org.ladocuploader.app.cli;

import com.google.common.collect.Lists;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.ladocuploader.app.csv.CsvDocument;
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
        log.info("Total submissions to transmit is {}", allSubmissions.size());

        log.info("Transmitting submissions for ECE");
        transmitBatch(allSubmissions, TransmissionType.ECE);
    }

    private void transmitBatch(List<Submission> submissions, TransmissionType transmissionType) throws IOException, JSchException, SftpException{
        String zipFilename = createZipFilename(transmissionType);
        // TODO: get errors for the packageType
        List<UUID> successfullySubmittedIds = zipFiles(submissions, zipFilename);

        // send zip file
        log.info("Uploading zip file");
        sftpClient.uploadFile(zipFilename);

        UUID runId = UUID.randomUUID();

        // Update transmission in DB
        successfullySubmittedIds.forEach(id -> {
            Submission submission = Submission.builder().id(id).build();
            Transmission transmission = transmissionRepository.findBySubmissionAndTransmissionType(submission, transmissionType);
            transmission.setTimeSent(new Date());
            transmission.setStatus(TransmissionStatus.Complete);
            transmission.setRunId(runId);
            transmissionRepository.save(transmission);
        });
        log.info("Finished transmission of a batch");
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
//        Map<CsvType, Map<UUID, String>> errorMessages = csvPackage.getErrorMessages();
        csvTypes.forEach(csvType ->
                {
                    try {
                        byte[] document = csvPackage.getCsvDocument(csvType).getCsvData();
                        ZipEntry entry = new ZipEntry(csvType.getFileName());
                        entry.setSize(document.length);
                        zipOutput.putNextEntry(entry);
                        zipOutput.write(document);
                        zipOutput.closeEntry();
                        // TODO: should we add errors at this stage as well if something fails with zip?
                    } catch (IOException e) {

//                        errorMessages.merge()
                        throw new RuntimeException(e);
                    }

                }
        );
    }

    private List<UUID> zipFiles(List<Submission> submissions, String zipFileName) throws IOException {
        List<UUID> successfullySubmittedIds = new ArrayList<>();
        Map<UUID, Map<CsvType, String>> submissionErrors = new HashMap<>();
        // TODO: collect successfully submitted IDs
        try (FileOutputStream baos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            CsvPackage ecePackage = csvService.generateCsvPackage(submissions, CsvPackageType.ECE_PACKAGE);
            Map<CsvType, Map<UUID, String>> errorMessages = ecePackage.getErrorMessages();
            // TODO: reformat error messages for easy update in transmission table
            errorMessages.forEach((csvType, submissionErrorMessages) -> submissionErrorMessages.forEach((submissionId, messages) -> {
                // TODO: remove the submission ID from successfully submitted?
//                submissionErrors.(submissionId, )
            }));
//            errorMessages.values().stream().flatMap(v->v.values().stream())

//            List<UUID> successfullySubmittedIds = new ArrayList<>();


            addZipEntries(ecePackage, zos);

        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }

        return successfullySubmittedIds;
    }

}

