package org.ladocuploader.app.submission;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class StringEncryptor {

  public static final int BLOCK_SIZE = 128;

  private final SecretKey secretKey;
  private final IvParameterSpec ivParameterSpec;

  Cipher desCipher;

  public StringEncryptor(String key) {
    try {
      secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
      desCipher = Cipher.getInstance("AES/GCM/NoPadding");
      ivParameterSpec = new IvParameterSpec(getRandomIv());
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new IllegalStateException("Unable to initialize encryptor", e);
    }
  }

  public StringEncryptor(String key, byte[] iv) {
    try {
      secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
      desCipher = Cipher.getInstance("AES/GCM/NoPadding");
      ivParameterSpec = new IvParameterSpec(iv);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new IllegalStateException("Unable to initialize encryptor", e);
    }
  }

  public String decrypt(String input) {
    byte[] bytes = runEncryption(Cipher.DECRYPT_MODE, Base64.decodeBase64(input),
        ivParameterSpec.getIV());
    return new String(bytes);
  }

  public String encrypt(String input) {
    byte[] bytes = runEncryption(Cipher.ENCRYPT_MODE, input.getBytes(), ivParameterSpec.getIV());
    return new String(Base64.encodeBase64(bytes));
  }

  private byte[] runEncryption(int mode, byte[] input, byte[] iv) {
    try {
      desCipher.init(mode, secretKey, new GCMParameterSpec(BLOCK_SIZE, iv));
      return desCipher.doFinal(input);
    } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException |
             InvalidAlgorithmParameterException e) {
      throw new IllegalStateException(e);
    }
  }

  private byte[] getRandomIv() {
    SecureRandom randomSecureRandom = new SecureRandom();
    byte[] iv = new byte[BLOCK_SIZE];
    randomSecureRandom.nextBytes(iv);
    return iv;
  }

  public byte[] getIV() {
    return ivParameterSpec.getIV();
  }
}
