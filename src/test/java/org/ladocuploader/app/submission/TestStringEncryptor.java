package org.ladocuploader.app.submission;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"test"})
public class TestStringEncryptor implements StringEncryptor {
  @Override
  public String decrypt(String ciphertext) {
    return ciphertext != null ? ciphertext.replace("encrypted_", "") : "";
  }

  @Override
  public String encrypt(String plaintext) {
    return "encrypted_" + plaintext;
  }
}
