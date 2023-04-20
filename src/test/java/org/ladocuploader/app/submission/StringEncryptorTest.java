package org.ladocuploader.app.submission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class StringEncryptorTest {

  @Test
  void testEncryptDecrypt() {
    StringEncryptor encryptor = new StringEncryptor("some-secret-key-that-is-32-bytes");
    StringEncryptor encryptor2 = new StringEncryptor("some-secret-key-that-is-32-bytes");

    String input = "this is not encrypted";
    String encrypted = encryptor.encrypt(input);
    String encrypted2 = encryptor2.encrypt(input);
    String decrypted = encryptor.decrypt(encrypted);
    String decrypted2 = encryptor2.decrypt(encrypted2);

    assertNotEquals(encrypted, decrypted);
    assertNotEquals(encrypted, encrypted2);
    assertEquals(input, decrypted);
    assertEquals(input, decrypted2);

  }

}