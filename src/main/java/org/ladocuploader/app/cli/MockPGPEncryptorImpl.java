package org.ladocuploader.app.cli;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@Profile({"dev", "test"})
public class MockPGPEncryptorImpl implements PGPEncryptor {

  @Override
  public byte[] signAndEncryptPayload(String filename) throws IOException {
    try (InputStream inputStream = new FileInputStream(filename)) {
      return inputStream.readAllBytes();
    }
  }
}
