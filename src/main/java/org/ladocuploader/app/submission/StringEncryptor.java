package org.ladocuploader.app.submission;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class StringEncryptor {

  private final SecretKey secretKey;

  public StringEncryptor(String key) {
    secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
  }

  public String decrypt(String input) {
    byte[] bytes = runEncryption(Cipher.DECRYPT_MODE, Base64.decodeBase64(input));
    return new String(bytes);
  }

  public String encrypt(String input) {
    byte[] bytes = runEncryption(Cipher.ENCRYPT_MODE, input.getBytes());
    return new String(Base64.encodeBase64(bytes));
  }

  private byte[] runEncryption(int mode, byte[] input) {
    try {
      Cipher desCipher = Cipher.getInstance("AES");
      desCipher.init(mode, secretKey);
      return desCipher.doFinal(input);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
             BadPaddingException | IllegalBlockSizeException e) {
      throw new IllegalStateException(e);
    }
  }
}
