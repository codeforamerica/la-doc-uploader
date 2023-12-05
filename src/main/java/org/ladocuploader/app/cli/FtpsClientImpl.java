package org.ladocuploader.app.cli;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@Component
//@Profile("production")
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

    File file = new File(zipFilename);

    ftp.execPBSZ(0);
    ftp.execPROT("P");
    ftp.enterLocalPassiveMode();
    ftp.pasv();
    InputStream local = new ByteArrayInputStream(data);
    ftp.storeFile(zipFilename, local);
    local.close();
    ftp.completePendingCommand();

    ftp.logout();
    ftp.disconnect();
  }
}
