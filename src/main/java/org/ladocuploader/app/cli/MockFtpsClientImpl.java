package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Component
@Profile({"dev", "test", "demo"})
public class MockFtpsClientImpl implements FtpsClient {
  public static final String MOCK_SERVER_NAME = "mockTransferServer";

  @Override
  public void uploadFile(String zipFilename, byte[] data) {
    log.info("Mock uploading file " + zipFilename);
    try {
      File dir = new File(MOCK_SERVER_NAME);
      if (!dir.exists() && !dir.mkdir()) {
        throw new IllegalStateException("Cannot make directory");
      }
      File file = new File(MOCK_SERVER_NAME + "/" + zipFilename);
      if (!file.exists() && !file.createNewFile()) {
        throw new IllegalStateException("Cannot create file");
      }
      FileOutputStream out = new FileOutputStream(file);
      out.write(data);
      out.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
