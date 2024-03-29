package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev", "test", "demo"})
public class MockSftpClientImpl implements SftpClient {

  @Override
  public void uploadFile(String zipFilename, String filePath) {
    // Do nothing
    log.info("Mock uploading file " + zipFilename);
  }

  @Override
  public void uploadFile(String zipFilename, String uploadLocation, byte [] data) {
    // Do nothing
    log.info("Mock uploading file " + zipFilename);
  }
}
