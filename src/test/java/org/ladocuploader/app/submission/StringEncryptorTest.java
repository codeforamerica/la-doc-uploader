package org.ladocuploader.app.submission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class StringEncryptorTest {

  StringEncryptor encryptor = new StringEncryptor("some-secret-key-that-is-32-bytes");

  @Test
  void testEncryptDecrypt() {
    String input = "this is not encrypted";
    String encrypted = encryptor.encrypt(input);
    String decrypted = encryptor.decrypt(encrypted);

    assertNotEquals(encrypted, decrypted);
    assertEquals(input, decrypted);
  }

}