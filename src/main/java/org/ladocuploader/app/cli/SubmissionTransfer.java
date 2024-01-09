package org.ladocuploader.app.cli;

import com.google.common.base.Strings;
import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepository;
import formflow.library.data.UserFile;
import formflow.library.data.UserFileRepositoryService;
import formflow.library.file.CloudFile;
import formflow.library.file.CloudFileRepository;
import formflow.library.pdf.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.data.Transmission;
import org.ladocuploader.app.data.TransmissionRepository;
import org.ladocuploader.app.data.enums.TransmissionStatus;
import org.ladocuploader.app.data.enums.TransmissionType;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.data.domain.Sort;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.ladocuploader.app.file.DocTypeEnum.*;

@Slf4j
@ShellComponent
public class SubmissionTransfer {
  private static final Map<String, String> DOCTYPE_FORMAT_MAP = new HashMap<>();

  static {
    DOCTYPE_FORMAT_MAP.put(BIRTH_CERTIFICATE.getValue(), "PD-Birth Verification");
    DOCTYPE_FORMAT_MAP.put(DRIVERS_LICENSE.getValue(), "PD-Identification");
    DOCTYPE_FORMAT_MAP.put(SOCIAL_SECURITY_CARD.getValue(), "PD-SSN");
    DOCTYPE_FORMAT_MAP.put(CHECK_STUB.getValue(), "INC-Wage Documents");
    DOCTYPE_FORMAT_MAP.put(OTHER_INCOME.getValue(), "INC - Other Unearned Income");
    DOCTYPE_FORMAT_MAP.put(BILL.getValue(), "EXP - Utility");
    DOCTYPE_FORMAT_MAP.put(MEDICAL_INFO.getValue(), "MED - Medical Forms");
    DOCTYPE_FORMAT_MAP.put(BANKING_INFO.getValue(), "RES - Bank Statement");
    DOCTYPE_FORMAT_MAP.put(MARRIAGE_LICENSE.getValue(), "LEG - Marriage License");
    DOCTYPE_FORMAT_MAP.put(DIVORCE_DECREE.getValue(), "LEG - Divorce Decree");
    DOCTYPE_FORMAT_MAP.put(COURT_ORDER.getValue(), "LEG - Court Order");
    DOCTYPE_FORMAT_MAP.put(PATERNITY.getValue(), "PAT - Acknowledgement of Paternity (ES)");
    DOCTYPE_FORMAT_MAP.put(OTHER.getValue(), "CORR - Customer Correspondence");
  }

  private final int BATCH_INDEX_LEN = "00050000000".length();

  private final long TWO_HOURS = (1000 * 60 * 60) * 2;

  private final SimpleDateFormat MMDDYYYY_HHMMSS = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

  private final TransmissionRepository transmissionRepository;
  private final UserFileRepositoryService fileRepositoryService;
  private final CloudFileRepository fileRepository;
  private final PdfService pdfService;
  private final PGPEncryptor pgpEncryptor;

  private final StringEncryptor encryptor;
  private final FtpsClient ftpsClient;

  public SubmissionTransfer(SubmissionRepository submissionRepository, TransmissionRepository transmissionRepository, UserFileRepositoryService fileRepositoryService, CloudFileRepository fileRepository, PdfService pdfService, PGPEncryptor pgpEncryptor, StringEncryptor encryptor, FtpsClient ftpsClient) {
    this.transmissionRepository = transmissionRepository;
    this.fileRepositoryService = fileRepositoryService;
    this.fileRepository = fileRepository;
    this.pdfService = pdfService;
    this.pgpEncryptor = pgpEncryptor;
    this.encryptor = encryptor;
    this.ftpsClient = ftpsClient;
  }

  @ShellMethod(key = "transferSubmissions")
  public void transferSubmissions() {
    // Get submissions to transfer
    String batchSeq = Long.toString(transmissionRepository.nextValueBatchSequence());
    String batchIndex = Strings.padStart(batchSeq, BATCH_INDEX_LEN, '0');
    UUID uuid = UUID.randomUUID();
    String zipFileName = batchIndex + ".zip";
    log.info(String.format("Beginning transfer of %s: batch %s", uuid, batchIndex));

    // Stats on transfers
    int successful = 0, failed = 0;

    // for each submission, add to zipfile
    List<Submission> submissionsBatch = transmissionRepository.submissionsToTransmit(Sort.unsorted(), TransmissionType.SNAP);
    int subfolderidx = 1;
    try (FileOutputStream baos = new FileOutputStream(zipFileName);
         ZipOutputStream zos = new ZipOutputStream(baos)) {
      long now = new Date().getTime();
      StringBuilder docMeta = new StringBuilder();
      for (Submission submission : submissionsBatch) {
        if (submission.getSubmittedAt().getTime() + TWO_HOURS > now) {
          // Give a 2-hour wait for folks to upload documents
          continue;
        }

        Transmission transmission = transmissionRepository.findBySubmissionAndTransmissionType(submission, TransmissionType.SNAP);

        String subfolder = Integer.toString(subfolderidx++);
        try {
          log.info("Generate applicant summary");
          packageSnapApplication(batchIndex, zos, docMeta, submission, subfolder);

          log.info("Adding uploaded docs");
          packageUploadedDocuments(batchIndex, zos, docMeta, submission, subfolder);

          transmission.setStatus(TransmissionStatus.Complete);
          updateTransmission(uuid, transmission);
          successful++;
        } catch (Exception e) {
          log.error("Error generating file collection for submission ID {}", submission.getId(), e);

          transmission.setDocumentationErrors(Map.of("error", e.getMessage(),"subfolder", subfolder));
          transmission.setStatus(TransmissionStatus.Failed);
          updateTransmission(uuid, transmission);
          failed++;
        }
      }

      // Add metadata entry to zip
      ZipEntry docEntry = new ZipEntry(batchIndex + ".txt");
      byte[] docMetaData = docMeta.toString().getBytes();
      docEntry.setSize(docMetaData.length);
      zos.putNextEntry(docEntry);
      zos.write(docMetaData);
      zos.closeEntry();
      zos.close();

      // Encrypt and transfer
      byte[] data = pgpEncryptor.signAndEncryptPayload(zipFileName);
      ftpsClient.uploadFile(zipFileName, data);
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    } finally {
      // Cleanup
      if (new File(zipFileName).delete()) {
        log.info("Deleting zip file");
      }
    }

    log.info(String.format("Completed transfer of batch %s, total %s, successful %s, failed %s", batchIndex, subfolderidx - 1, successful, failed));
  }

  private void updateTransmission(UUID uuid, Transmission transmission) {
    transmission.setTimeSent(new Date());
    transmission.setRunId(uuid);
    transmissionRepository.save(transmission);
  }

  private void packageUploadedDocuments(String batchIndex, ZipOutputStream zos, StringBuilder docMeta, Submission submission, String subfolder) throws IOException {
    List<UserFile> userFiles = fileRepositoryService.findAllBySubmission(submission);
    Map<String, Integer> filenameDuplicates = new HashMap<>();
    for (UserFile userFile : userFiles) {
      // Account for files of the same name
      String docUploadFilename = userFile.getOriginalName();
      filenameDuplicates.putIfAbsent(docUploadFilename, 0);
      filenameDuplicates.computeIfPresent(docUploadFilename, (s, i) -> i + 1);
      Integer filecount = filenameDuplicates.get(docUploadFilename);
      if (filecount > 1) {
        docUploadFilename = "%s_%s".formatted(filecount, docUploadFilename);
      }

      ZipEntry docEntry = new ZipEntry(subfolder + "/" + docUploadFilename);
      docEntry.setSize(userFile.getFilesize().longValue());
      zos.putNextEntry(docEntry);

      CloudFile docFile = fileRepository.get(userFile.getRepositoryPath());
      zos.write(docFile.getFileBytes());
      zos.closeEntry();

      // write doc metadata
      String docType = (String) submission.getInputData().get("docType_wildcard_" + userFile.getFileId());
      String metaEntry = generateMetaDataEntry(batchIndex, subfolder, docUploadFilename, DOCTYPE_FORMAT_MAP.get(docType), submission);
      docMeta.append(metaEntry);
    }
  }

  private void packageSnapApplication(String batchIndex, ZipOutputStream zos, StringBuilder docMeta, Submission submission, String subfolder) throws IOException {
    byte[] file = pdfService.getFilledOutPDF(submission); // TODO Handle generate-pdf crashes
    String fileName = "SNAP_application.pdf";
    zos.putNextEntry(new ZipEntry(subfolder + "/"));
    ZipEntry entry = new ZipEntry(subfolder + "/" + fileName);
    entry.setSize(file.length);
    zos.putNextEntry(entry);
    zos.write(file);
    zos.closeEntry();
    String metaEntry = generateMetaDataEntry(batchIndex, subfolder, fileName, "APP-OFS 4APP", submission);
    docMeta.append(metaEntry);
  }

  private String generateMetaDataEntry(String batchIndex, String subfolder, String filename, String documentType, Submission submission) {
    Map<String, Object> inputData = submission.getInputData();
    String formattedSSN = encryptor.decrypt((String) inputData.getOrDefault("encryptedSSN", ""));
    String formattedFilename = removeFileExtension(filename);
    String formattedBirthdate = formatBirthdate(submission.getInputData());
    String formattedSubmissionDate = MMDDYYYY_HHMMSS.format(submission.getSubmittedAt());
    String filelocation = String.format("\"%s/%s/%s\",", batchIndex, subfolder, filename);

    return "\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n"
        .formatted(
            batchIndex,
            formattedFilename,
            documentType,
            inputData.getOrDefault("firstName", ""),
            inputData.getOrDefault("lastName", ""),
            formattedSSN,
            formattedBirthdate,
            formattedSubmissionDate,
            filelocation);
  }

  private static String removeFileExtension(String filename) {
    int extIdx = filename.indexOf('.');
    if (extIdx != -1) {
      return filename.substring(0, extIdx);
    }
    return filename;
  }

  private static String formatBirthdate(Map<String, Object> inputData) {
    String day = (String) inputData.getOrDefault("birthDay", "");
    String month = (String) inputData.getOrDefault("birthMonth", "");
    String year = (String) inputData.getOrDefault("birthYear", "");

    return "%s/%s/%s".formatted(Strings.padStart(day, 2, '0'), Strings.padStart(month, 2, '0'), year);
  }
}
