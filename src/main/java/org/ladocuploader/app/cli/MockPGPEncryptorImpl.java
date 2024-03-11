package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@Profile({"dev", "test"})
public class MockPGPEncryptorImpl extends BasePGPEncrpytorImpl {

  public MockPGPEncryptorImpl() {
    super(null, null, null, null, null, null, null);
  }

  @Override
  public void init() {
    log.info("do nothing");
  }

  @Override
  public byte[] signAndEncryptPayload(String filename) throws IOException {
    log.info("Mock encrypting payload " + filename);
    try (InputStream inputStream = new FileInputStream(filename)) {
      return inputStream.readAllBytes();
    }
  }
}
