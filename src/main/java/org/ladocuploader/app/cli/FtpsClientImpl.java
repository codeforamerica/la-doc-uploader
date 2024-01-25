package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@Slf4j
@Component
@Profile({"production", "staging"})
public class FtpsClientImpl implements FtpsClient {

  private final String username;
  private final String password;
  private final String uploadUrl;
  private final String uploadDir;

  public FtpsClientImpl(@Value("${ftps.username:}") String username, @Value("${ftps.password:}") String password, @Value("${ftps.upload-url:}") String uploadUrl, @Value("${ftps.upload-dir:}") String uploadDir) {
    this.username = username;
    this.password = password;
    this.uploadUrl = uploadUrl;
    this.uploadDir = uploadDir;
  }

  @Override
  public void uploadFile(String zipFilename, byte[] data) throws IOException {
    FTPSClient ftp = new FTPSClient();

    ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

    ftp.connect(uploadUrl);
    int reply = ftp.getReplyCode();
    if (!FTPReply.isPositiveCompletion(reply)) {
      ftp.disconnect();
      throw new IOException("Exception in connecting to FTP Server");
    }

    ftp.login(username, password);

    ftp.changeWorkingDirectory(uploadDir);

    ftp.execPBSZ(0);
    ftp.execPROT("P");
    ftp.enterLocalPassiveMode();
    ftp.setFileType(FTP.BINARY_FILE_TYPE);
    ftp.pasv();
    InputStream local = new ByteArrayInputStream(data);
    boolean isComplete = ftp.storeFile(zipFilename, local);
    local.close();

    try {
      if (isComplete || ftp.completePendingCommand()) {
        ftp.logout();
          ftp.disconnect();
        log.info("Upload completed with isComplete: %s. Disconnected.".formatted(isComplete));
      }
    } catch (IOException e) {
      log.error("Failed to finalize transfer", e);
    }
  }
}
