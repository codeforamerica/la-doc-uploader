package org.ladocuploader.app.submission;

public interface StringEncryptor {


  public String decrypt(String ciphertext);
  public String encrypt(String plaintext);
}
