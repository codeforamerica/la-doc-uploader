package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Component
@Profile({"dev", "test", "demo"})
public class MockFtpsClientImpl implements FtpsClient {

  @Override
  public void uploadFile(String zipFilename, byte[] data) {
    // Do nothing
    log.info("Mock uploading file " + zipFilename);
    try {
      FileOutputStream out = new FileOutputStream("mockTransferServer/" + zipFilename);
      out.write(data);
      out.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
