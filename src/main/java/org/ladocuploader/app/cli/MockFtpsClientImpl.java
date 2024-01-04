package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev", "test"})
public class MockFtpsClientImpl implements FtpsClient {

  @Override
  public void uploadFile(String zipFilename, byte[] data) {
    // Do nothing
    log.info("Mock uploading file " + zipFilename);
  }
}
