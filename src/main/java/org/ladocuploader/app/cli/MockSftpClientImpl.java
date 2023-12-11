package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("test")
public class MockSftpClientImpl implements SftpClient {

  @Override
  public void uploadFile(String zipFilename, String filePath) {
    // Do nothing
    log.info("Mock uploading file " + zipFilename);
  }
}
