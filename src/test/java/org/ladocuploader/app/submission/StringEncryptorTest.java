package org.ladocuploader.app.submission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class StringEncryptorTest {

  @Test
  void testEncryptDecrypt() {
    StringEncryptor encryptor = new StringEncryptor("arn:aws:kms:us-west-2:111122223333:key/1234abcd-12ab-34cd-56ef-1234567890ab");
    StringEncryptor encryptor2 = new StringEncryptor("arn:aws:kms:us-west-2:111122223333:key/1234abcd-12ab-34cd-56ef-1234567890ab");

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