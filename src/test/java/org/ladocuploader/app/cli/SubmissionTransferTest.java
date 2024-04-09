package org.ladocuploader.app.cli;

import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepository;
import formflow.library.data.UserFile;
import formflow.library.data.UserFileRepository;
import formflow.library.file.CloudFile;
import formflow.library.file.CloudFileRepository;
import formflow.library.pdf.PdfService;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.Transmission;
import org.ladocuploader.app.data.TransmissionRepository;
import org.ladocuploader.app.data.enums.TransmissionStatus;
import org.ladocuploader.app.data.enums.TransmissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.ladocuploader.app.cli.MockFtpsClientImpl.MOCK_SERVER_NAME;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class SubmissionTransferTest {

  @Autowired
  SubmissionTransfer submissionTransfer;

  @Autowired
  SubmissionRepository submissionRepository;

  @Autowired
  TransmissionRepository transmissionRepository;

  @Autowired
  UserFileRepository userFileRepository;

  @Autowired
  FtpsClient ftpsClient;

  @MockBean
  PdfService pdfService;

  @MockBean
  CloudFileRepository fileRepository;

  private Submission submissionWithDocs;
  private Submission submissionWithoutDocs;
  private Submission invalidSubmission;
  private Submission submissionWithMissingFields;
  private Submission submittedBeforeDelayCutoff;

  @BeforeEach
  void setup() throws IOException {
    submissionWithoutDocs = queueSubmissionWithoutDocs();
    submissionWithDocs = queueSubmissionWithDocs();
    invalidSubmission = queueInvalidSubmission();
    submissionWithMissingFields = queueInvalidSubmission();
    submittedBeforeDelayCutoff = queueSubmittedNow();

    when(pdfService.getFilledOutPDF(eq(submissionWithoutDocs))).thenReturn("some bytes".getBytes());
    when(pdfService.getFilledOutPDF(eq(submissionWithDocs))).thenReturn("some other bytes".getBytes());
    when(pdfService.getFilledOutPDF(eq(invalidSubmission))).thenThrow(new IllegalArgumentException("There was an error generating the PDF"));
    when(pdfService.getFilledOutPDF(eq(submissionWithMissingFields))).thenThrow(new IllegalArgumentException("Required field(s) are missing"));
  }

  @Test
  public void transmitZipFile() throws IOException {
//    Long expectedNextBatchSeq = transmissionRepository.nextValueBatchSequence() + 2;

    submissionTransfer.transferSubmissions();

//    assertThat(transmissionRepository.nextValueBatchSequence(), equalTo(expectedNextBatchSeq));

    File zipFile = new File(MOCK_SERVER_NAME + "/00050000000.zip.gpg");
    assertTrue(zipFile.exists());

    Transmission transmittedWithDocs = transmissionRepository.findBySubmissionAndTransmissionType(submissionWithDocs, TransmissionType.SNAP);
    assertThat(transmittedWithDocs.getStatus(), equalTo(TransmissionStatus.Complete));
    assertNull(transmittedWithDocs.getDocumentationErrors());

    Transmission transmittedWithoutDocs = transmissionRepository.findBySubmissionAndTransmissionType(submissionWithoutDocs, TransmissionType.SNAP);
    assertThat(transmittedWithoutDocs.getStatus(), equalTo(TransmissionStatus.Complete));
    assertNull(transmittedWithoutDocs.getDocumentationErrors());

    Transmission invalidTransmission = transmissionRepository.findBySubmissionAndTransmissionType(invalidSubmission, TransmissionType.SNAP);
    assertThat(invalidTransmission.getStatus(), equalTo(TransmissionStatus.Failed));
    assertThat(invalidTransmission.getDocumentationErrors().get("error"), equalTo("There was an error generating the PDF"));
    assertThat(invalidTransmission.getDocumentationErrors().get("subfolder"), equalTo("3"));

    Transmission missingFields = transmissionRepository.findBySubmissionAndTransmissionType(submissionWithMissingFields, TransmissionType.SNAP);
    assertThat(missingFields.getStatus(), equalTo(TransmissionStatus.Failed));
    assertThat(missingFields.getDocumentationErrors().get("error"), equalTo("Required field(s) are missing"));
    assertThat(missingFields.getDocumentationErrors().get("subfolder"), equalTo("4"));

    Transmission notTransmittedYet = transmissionRepository.findBySubmissionAndTransmissionType(submittedBeforeDelayCutoff, TransmissionType.SNAP);
    assertThat(notTransmittedYet.getStatus(), equalTo(TransmissionStatus.Queued));

    String destDir = "output";
    List<String> fileNames = unzip(zipFile.getPath(), destDir);

    assertThat(fileNames, hasItem("output/00050000000/1/"));
    assertThat(fileNames, hasItem("output/00050000000/1/SNAP_application.pdf"));
    assertThat(fileNames, hasItem("output/00050000000/2/"));
    assertThat(fileNames, hasItem("output/00050000000/2/SNAP_application.pdf"));
    assertThat(fileNames, hasItem("output/00050000000/2/originalFilename.png"));
    assertThat(fileNames, hasItem("output/00050000000/2/2_originalFilename.png"));
    assertThat(fileNames, hasItem("output/00050000000/2/weird/:\\filename.jpg"));
    assertThat(fileNames, hasItem("output/00050000000.txt"));
    assertEquals(8, fileNames.size());

    // cleanup
    zipFile.delete();
    FileUtils.deleteDirectory(new File(MOCK_SERVER_NAME));
  }

  @Test
  public void failedToTransmitZipFile() throws IOException {
    FileOutputStream dummyFile = mockTransferExcetion();
//    Long expectedNextBatchSeq = transmissionRepository.nextValueBatchSequence() + 1;

    submissionTransfer.transferSubmissions();

    // Should not increment
//    assertThat(transmissionRepository.nextValueBatchSequence(), equalTo(expectedNextBatchSeq));

    // cleanup
    dummyFile.close();
  }

  @NotNull
  private static FileOutputStream mockTransferExcetion() throws IOException {
    // Mock error on package transfer
    FileUtils.deleteDirectory(new File(MOCK_SERVER_NAME));
    File file = new File(MOCK_SERVER_NAME);
    file.deleteOnExit();
    file.createNewFile();
    FileOutputStream dummyFile = new FileOutputStream(MOCK_SERVER_NAME);
    dummyFile.write("Some info that isn't a directory".getBytes());
    return dummyFile;
  }

  private Submission queueInvalidSubmission() {
    OffsetDateTime submittedDate = OffsetDateTime.now().minusHours(2L);
    Submission submission = Submission.builder()
        .submittedAt(submittedDate)
        .flow("laDigitalAssister")
        .urlParams(new HashMap<>())
        .inputData(Map.of(
            "firstName", "test",
            "lastName", "test",
            "homeAddressStreetAddress1", "123 Foo Street",
            "signature", "Tester McTest sig")).build();
    submissionRepository.save(submission);
    saveTransmissionRecord(submission);
    return submission;
  }

  private Submission queueSubmittedNow() {
    OffsetDateTime submittedDate = OffsetDateTime.now();
    Submission submission = Submission.builder()
        .submittedAt(submittedDate)
        .flow("laDigitalAssister")
        .urlParams(new HashMap<>())
        .inputData(Map.of(
            "firstName", "test",
            "lastName", "test",
            "homeAddressStreetAddress1", "123 Foo Street",
            "signature", "Tester McTest sig")).build();
    submissionRepository.save(submission);
    saveTransmissionRecord(submission);
    return submission;
  }

  private Submission queueSubmissionWithDocs() {
    OffsetDateTime submittedDate = OffsetDateTime.now().minusHours(2L);
    var submission = Submission.builder()
        .submittedAt(submittedDate)
        .flow("laDigitalAssister")
        .urlParams(new HashMap<>())
        .inputData(new HashMap<>(Map.of(
            "firstName", "Other",
            "lastName", "McOtherson",
            "birthDay", "1",
            "birthMonth", "11",
            "birthYear", "1111",
            "homeAddressStreetAddress1", "123 Foo Street",
            "signature", "Other McOtherson sig"
        ))).build();
    submissionRepository.save(submission);
    UUID docId1 = saveUserFile(submission, "applicant1_birth-certificate.jpeg", "originalFilename.png");
    UUID docId2 = saveUserFile(submission, "applicant1_license.jpeg", "originalFilename.png");
    UUID docId3 = saveUserFile(submission, "applicant1_pay-stub.png", "weird/:\\filename.jpg");
    saveTransmissionRecord(submission);
    submission.getInputData().putAll(Map.of("uploadDocuments", "[\"%s\",\"%s\",\"%s\"]".formatted(docId1, docId2, docId3),
        "docType_wildcard_" + docId1, "BirthCertificate",
        "docType_wildcard_" + docId2, "Other",
        "docType_wildcard_" + docId3, "DriversLicense"));
    submissionRepository.save(submission);

    Stream.of("applicant1_birth-certificate.jpeg", "applicant1_license.jpeg", "applicant1_pay-stub.png")
        .forEach(filename -> {
          try (FileInputStream docFileStream = new FileInputStream("src/test/resources/inputs/" + filename)) {
            byte[] docFileData = docFileStream.readAllBytes();
            when(fileRepository.get(eq(filename))).thenReturn(new CloudFile(10L, docFileData));
          } catch (IOException e) {
            throw new IllegalStateException(e);
          }
        });
    return submission;
  }

  private Submission queueSubmissionWithoutDocs() {
    OffsetDateTime submittedDate = OffsetDateTime.now().minusHours(2L);
    Submission submission = Submission.builder()
        .submittedAt(submittedDate)
        .flow("laDigitalAssister")
        .urlParams(new HashMap<>())
        .inputData(Map.of(
            "firstName", "Tester",
            "lastName", "McTest",
            "birthDay", "3",
            "birthMonth", "12",
            "birthYear", "4567",
            "mailingAddressStreetAddress1", "123 Foo Street",
            "signature", "Tester McTest sig"
        )).build();
    submissionRepository.save(submission);
    saveTransmissionRecord(submission);
    return submission;
  }

  private void saveTransmissionRecord(Submission submission) {
    Transmission transmission = Transmission.fromSubmission(submission);
    transmission.setTransmissionType(TransmissionType.SNAP);
    transmission.setStatus(TransmissionStatus.Queued);
    transmissionRepository.save(transmission);
  }

  private UUID saveUserFile(Submission submission, String repoPath, String originalName) {
    UserFile docFile = new UserFile();
    docFile.setFilesize(10.0f);
    docFile.setSubmission(submission);
    docFile.setRepositoryPath(repoPath);
    docFile.setOriginalName(originalName);
    userFileRepository.save(docFile);
    return docFile.getFileId();
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
}
