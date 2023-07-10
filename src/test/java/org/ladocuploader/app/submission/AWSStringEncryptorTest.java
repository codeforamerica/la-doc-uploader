package org.ladocuploader.app.submission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AWSStringEncryptorTest {

  void testEncryptDecrypt() {
    String keyARN = "put arn here";
    String accessKey = "put access key here";
    String secretKey = "put secret key here";
    StringEncryptor encryptor = new AWSStringEncryptor(keyARN, accessKey, secretKey);
    StringEncryptor encryptor2 = new AWSStringEncryptor(keyARN, accessKey, secretKey);

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