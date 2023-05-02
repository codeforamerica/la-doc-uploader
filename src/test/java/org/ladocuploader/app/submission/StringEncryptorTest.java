package org.ladocuploader.app.submission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class StringEncryptorTest {

  StringEncryptor encryptor = new StringEncryptor("{\"primaryKeyId\":2135185311,\"key\":[{\"keyData\":{\"typeUrl\":\"type.googleapis.com/google.crypto.tink.AesGcmKey\",\"value\":\"GiCRKaXiJ/zlDHAZfRQf1rCIbIY4fFmLqLWYIPLNXpOx4A==\",\"keyMaterialType\":\"SYMMETRIC\"},\"status\":\"ENABLED\",\"keyId\":2135185311,\"outputPrefixType\":\"TINK\"}]}");

  @Test
  void testEncryptDecrypt() {
    String input = "this is not encrypted";
    String encrypted = encryptor.encrypt(input);
    String encrypted2 = encryptor.encrypt(input);
    String decrypted = encryptor.decrypt(encrypted);
    String decrypted2 = encryptor.decrypt(encrypted2);

    assertNotEquals(encrypted, decrypted);
    assertNotEquals(encrypted, encrypted2);
    assertEquals(input, decrypted);
    assertEquals(input, decrypted2);
  }

}