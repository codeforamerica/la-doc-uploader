package org.ladocuploader.app.cli;

import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepository;
import formflow.library.data.UserFile;
import formflow.library.data.UserFileRepository;
import formflow.library.file.CloudFile;
import formflow.library.file.CloudFileRepository;
import formflow.library.pdf.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.util.DateUtil.now;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class SubmissionTransferTest {

  @Autowired
  SubmissionTransfer submissionTransfer;

  @Autowired
  SubmissionRepository submissionRepository;

  @Autowired
  UserFileRepository userFileRepository;

  @MockBean
  PdfService pdfService;

  @MockBean
  CloudFileRepository fileRepository;

  @MockBean
  FtpsClient ftpsClient;


  Submission submission;

  @BeforeEach
  void setup() {
    String batchIndex = "0005";
    submission = Submission.builder()
        .submittedAt(now())
        .flow("laDigitalAssister")
        .urlParams(new HashMap<>())
        .inputData(Map.of(
            "firstName", "Tester",
            "lastName", "McTest",
            "signature", "Tester McTest sig"
        )).build();
    submissionRepository.save(submission);

    var submissionWithDocs = Submission.builder()
        .submittedAt(now())
        .flow("laDigitalAssister")
        .urlParams(new HashMap<>())
        .inputData(Map.of(
            "firstName", "Other",
            "lastName", "McOtherson",
            "signature", "Other McOtherson sig"
        )).build();
    submissionRepository.save(submissionWithDocs);

    UserFile docfile = new UserFile();
    docfile.setFilesize(10.0f);
    docfile.setSubmission(submissionWithDocs);
    docfile.setOriginalName("originalFilename.png");
    userFileRepository.save(docfile);

    UserFile docfileSameName = new UserFile();
    docfileSameName.setFilesize(10.0f);
    docfileSameName.setSubmission(submissionWithDocs);
    docfileSameName.setOriginalName("originalFilename.png");
    userFileRepository.save(docfileSameName);

    UserFile docfileWeirdFilename = new UserFile();
    docfileWeirdFilename.setFilesize(10.0f);
    docfileWeirdFilename.setSubmission(submissionWithDocs);
    docfileWeirdFilename.setOriginalName("weird/:\\filename.jpg");
    userFileRepository.save(docfileWeirdFilename);
  }

//  @Test
  void transmitZipFile() throws IOException {
    when(pdfService.getFilledOutPDF(any())).thenReturn("some bytes".getBytes());
    when(pdfService.generatePdfName(any())).thenReturn("applicant_summary");

    File docFile = new File("paystub.png");
    docFile.createNewFile();
    byte[] docFileData;
    try (FileInputStream docFileStream = new FileInputStream(docFile)) {
      docFileData = docFileStream.readAllBytes();
      when(fileRepository.get(any())).thenReturn(new CloudFile(10L, docFileData));
    }

    submissionTransfer.transferSubmissions();

    File zipFile = new File("0005.zip");
    assertTrue(zipFile.exists());

    verify(ftpsClient).uploadFile(zipFile.getName(), docFileData);

    String destDir = "output";
    List<String> fileNames = unzip(zipFile.getPath(), destDir);

    assertThat(fileNames, hasItem("output/1/"));
    assertThat(fileNames, hasItem("output/1/SNAP_application.pdf"));
    assertThat(fileNames, hasItem("output/1/doc.png"));
    assertThat(fileNames, hasItem("output/2/"));
    assertThat(fileNames, hasItem("output/2/SNAP_application.pdf"));
    assertThat(fileNames, hasItem("output/2/01_originalFilename.png"));
    assertThat(fileNames, hasItem("output/2/02_originalFilename.png"));
    assertThat(fileNames, hasItem("output/2/weird___filename.jpg"));
    assertThat(fileNames, not(hasItem("output/3/01_originalFilename.png")));
    assertThat(fileNames, not(hasItem("output/3/01_doc.png")));
    assertEquals(8, fileNames.size());

    // cleanup
    zipFile.delete();
    docFile.delete();
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
