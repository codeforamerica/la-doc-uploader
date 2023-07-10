package org.ladocuploader.app.submission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.Test;
import org.ladocuploader.app.utils.MockKMSClient;

class StringEncryptorTest {

  @Test
  void testEncryptDecrypt() {
    MockKMSClient client = spy(new MockKMSClient());
    String key1 = client.createKey().getKeyMetadata().getArn();
    StringEncryptor encryptor = new StringEncryptor(key1);
    StringEncryptor encryptor2 = new StringEncryptor(key1);

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