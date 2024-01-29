package org.ladocuploader.app.cli;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.UserFile;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepository;
import formflow.library.data.UserFileRepository;
import formflow.library.file.CloudFile;
import formflow.library.file.CloudFileRepository;
import lombok.extern.slf4j.Slf4j;
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
import org.ladocuploader.app.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static java.time.OffsetDateTime.now;

@ActiveProfiles("test")
@SpringBootTest
@Slf4j
public class TransmitterCommandsTest {

    @Autowired
    TransmitterCommands transmitterCommands;

    @Autowired
    SubmissionRepository submissionRepository;

    @MockBean
    CsvService csvService;

    @MockBean
    CloudFileRepository fileRepository;

    @Autowired
    UserFileRepository userFileRepository;

    @Autowired
    TransmissionRepository transmissionRepository;

    @MockBean
    SftpClient sftpClient;

    Submission submission;

    Submission submissionWithDocs;

    @BeforeEach
    void setup() {
        submission = Submission.builder()
                .submittedAt(now())
                .urlParams(new HashMap<>())
                .submittedAt(TestUtils.makeOffsetDateTime("2024-01-28"))
                .inputData(Map.of(
                        "firstName", "first",
                        "lastName", "last",
                        "signature", "first last"
                ))
                .build();
        submissionRepository.save(submission);
        submissionWithDocs = Submission.builder()
                .submittedAt(now())
                .urlParams(new HashMap<>())
                .submittedAt(TestUtils.makeOffsetDateTime("2024-01-28"))
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

        UserFile docfile = new UserFile();
        docfile.setFilesize(10.0f);
        docfile.setSubmission(submissionWithDocs);
        docfile.setRepositoryPath("originalFilename.png");
        docfile.setOriginalName("originalFilename.png");
        userFileRepository.save(docfile);

        transmission = Transmission.fromSubmission(submissionWithDocs);
        transmission.setTransmissionType(TransmissionType.ECE);
        transmission.setStatus(TransmissionStatus.Queued);
        transmissionRepository.save(transmission);


    }

    private static List<String> unzip(String zipFilePath, String destDir) {
        List<String> result = new ArrayList<>();
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try (FileInputStream fis = new FileInputStream(zipFilePath)) {
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                result.add(destDir + File.separator + fileName);
                while (zis.read(buffer) > 0) {
                }
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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

        byte[] bytes = Files.readAllBytes(docFile.toPath());

        when(fileRepository.get(any())).thenReturn(new CloudFile(10L, bytes));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        File zipFile = new File("Apps__ECE__" + date + ".zip");
        doAnswer(invocation -> {
            String destDir = "output";
            List<String> fileNames = unzip(zipFile.getPath(), destDir);
            assertThat(fileNames, hasItem("output/parent_guardian.csv"));
            assertThat(fileNames, hasItem("output/student.csv"));
            assertThat(fileNames, hasItem("output/relationship.csv"));
            assertThat(fileNames, hasItem("output/application.csv"));
            assertThat(fileNames, hasItem("output/%s_files/01_originalFilename.png".formatted(submissionWithDocs.getId())));
            return null;
        }).when(sftpClient).uploadFile(zipFile.getName(), "/la-du-moveit-transfer/nola-ps");
        transmitterCommands.transmit();

        docFile.delete();

        Transmission transmission = transmissionRepository.findBySubmissionAndTransmissionType(submission, TransmissionType.ECE);
        assertNotNull(transmission.getStatus());
        assertNotNull(transmission.getRunId());

    }
}
