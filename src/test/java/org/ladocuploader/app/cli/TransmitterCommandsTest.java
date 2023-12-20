package org.ladocuploader.app.cli;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.UserFile;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepository;
import formflow.library.data.UserFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.csv.CsvDocument;
import org.ladocuploader.app.csv.CsvPackage;
import org.ladocuploader.app.csv.CsvService;
import org.ladocuploader.app.csv.enums.CsvPackageType;
import org.ladocuploader.app.csv.enums.CsvType;
import org.ladocuploader.app.data.Transmission;
import org.ladocuploader.app.data.TransmissionRepository;
import org.ladocuploader.app.data.enums.TransmissionStatus;
import org.ladocuploader.app.data.enums.TransmissionType;
import org.ladocuploader.app.upload.CloudFile;
import org.ladocuploader.app.upload.ReadOnlyCloudFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
public class TransmitterCommandsTest {

    @Autowired
    TransmitterCommands transmitterCommands;

    @Autowired
    SubmissionRepository submissionRepository;

    @MockBean
    CsvService csvService;

    @MockBean
    ReadOnlyCloudFileRepository fileRepository;

    @Autowired
    UserFileRepository userFileRepository;

    @Autowired
    TransmissionRepository transmissionRepository;

    Submission submission;

//    List<Submission> submissionList;

    @BeforeEach
    void setup() {
        submission = Submission.builder()
                .submittedAt(now())
                .urlParams(new HashMap<>())
                .inputData(Map.of(
                        "firstName", "first",
                        "lastName", "last",
                        "signature", "first last"
                ))
                .build();
        submissionRepository.save(submission);
        var submissionWithDocs = Submission.builder()
                .submittedAt(now())
                .urlParams(new HashMap<>())
                .inputData(Map.of(
                        "firstName", "Other",
                        "lastName", "McOtherson",
                        "signature", "Other McOtherson sig",
                        "identityFiles", "[\"some-file-id\"]"
                )).build();
        submissionRepository.save(submissionWithDocs);
        Transmission transmission = Transmission.fromSubmission(submission);
        transmission.setTransmissionType(TransmissionType.ECE);
        transmission.setStatus(TransmissionStatus.Queued);
        transmissionRepository.save(transmission);
//        submissionList.add(submission);

        UserFile docfile = new UserFile();
        docfile.setFilesize(10.0f);
        docfile.setSubmission(submissionWithDocs);
        docfile.setOriginalName("originalFilename.png");
        userFileRepository.save(docfile);

        transmission = Transmission.fromSubmission(submissionWithDocs);
        transmission.setTransmissionType(TransmissionType.ECE);
        transmission.setStatus(TransmissionStatus.Queued);
        transmissionRepository.save(transmission);


    }

    @Test
    void transmitZipFile() throws IOException, JSchException, SftpException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        CsvPackage ecePackage = new CsvPackage(CsvPackageType.ECE_PACKAGE);
        List<CsvType> csvTypes = CsvPackageType.ECE_PACKAGE.getCsvTypeList();
        csvTypes.forEach(csvType ->
        {
            CsvDocument csvDocument = new CsvDocument(csvType, "some bytes".getBytes());
            ecePackage.addCsvDocument(csvDocument);
        });


        when(csvService.generateCsvPackage(any(), any())).thenReturn(ecePackage);

        File docFile = new File("paystub.png");
        docFile.createNewFile();

        when(fileRepository.download(any())).thenReturn(new CloudFile(10L, docFile));

        transmitterCommands.transmit();

//        transmi
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDateTime now = LocalDateTime.now();
//        String date = dtf.format(now);
//        File zipFile = new File("Apps__" + date + "__1001-1006.zip");
//        assertTrue(zipFile.exists());
    }







}
