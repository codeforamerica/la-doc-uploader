package org.ladocuploader.app.cli;

import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepository;
import formflow.library.data.UserFile;
import formflow.library.data.UserFileRepositoryService;
import formflow.library.file.CloudFile;
import formflow.library.file.CloudFileRepository;
import formflow.library.pdf.PdfService;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
//@ShellComponent
public class SubmissionTransfer {
  private final SubmissionRepository submissionRepository;
  private final UserFileRepositoryService fileRepositoryService;
  private final CloudFileRepository fileRepository;
  private final PdfService pdfService;
  private final PGPEncryptor encryptor;
  private final FtpsClient ftpsClient;

  public SubmissionTransfer(SubmissionRepository submissionRepository, UserFileRepositoryService fileRepositoryService, CloudFileRepository fileRepository, PdfService pdfService, PGPEncryptor encryptor, FtpsClient ftpsClient) {
    this.submissionRepository = submissionRepository;
    this.fileRepositoryService = fileRepositoryService;
    this.fileRepository = fileRepository;
    this.pdfService = pdfService;
    this.encryptor = encryptor;
    this.ftpsClient = ftpsClient;
  }

  public void transferSubmissions() {
    // TODO get submissions to transfer

    String batchIndex = "5000"; // TODO Prob needs a counter
    String zipFileName = batchIndex + ".zip";

    // for each, add to zipfile
    List<Submission> submissionsBatch = new ArrayList<>(); // TODO Get from transmission table
    try (FileOutputStream baos = new FileOutputStream(zipFileName);
         ZipOutputStream zos = new ZipOutputStream(baos)) {
      for (Submission submission : submissionsBatch) {
        String subfolder = "1"; // TODO Something unique
        try {
          if ("laDigitalAssister".equals(submission.getFlow())) {
            // Generate applicant summary
            byte[] file = pdfService.getFilledOutPDF(submission); // TODO Handle generate-pdf crashes
            String fileName = String.format("%s_application.pdf", subfolder);
            zos.putNextEntry(new ZipEntry(subfolder));
            ZipEntry entry = new ZipEntry(subfolder + fileName);
            entry.setSize(file.length);
            zos.putNextEntry(entry);
            zos.write(file);
            zos.closeEntry();

            // Add uploaded docs
            List<UserFile> userFiles = fileRepositoryService.findAllBySubmission(submission);
            for (UserFile userFile : userFiles) {
              String docUploadFilename = "somethingunique"; // TODO this can be anything
              ZipEntry docEntry = new ZipEntry(subfolder + docUploadFilename);
              docEntry.setSize(userFile.getFilesize().longValue());
              zos.putNextEntry(docEntry);

              CloudFile docFile = fileRepository.get(userFile.getRepositoryPath());
              zos.write(docFile.getFileBytes());
              zos.closeEntry();
            }
          }

        } catch (Exception e) {
          log.error("Error generating file collection for submission ID {}", submission.getId(), e);
        }
        // Encrypt and transfer
        zos.close();
        byte[] data = encryptor.encryptPayload(zipFileName);
        ftpsClient.uploadFile(zipFileName, data);

      }
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }
}
