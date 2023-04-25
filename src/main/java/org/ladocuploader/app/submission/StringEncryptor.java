package org.ladocuploader.app.submission;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.aead.AeadConfig;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class StringEncryptor {

  private final Aead encDec;

  public StringEncryptor(String key) {
    try {
      AeadConfig.register();
      encDec = CleartextKeysetHandle.read(JsonKeysetReader.withString(key))
          .getPrimitive(Aead.class);
    } catch (GeneralSecurityException | IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public String encrypt(String data) {
    try {
      return new String(Hex.encodeHex(encDec.encrypt(data.getBytes(), null)));
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  public String decrypt(String encryptedData) {
    try {
      return new String(encDec.decrypt(Hex.decodeHex(encryptedData.toCharArray()), null));
    } catch (GeneralSecurityException | DecoderException e) {
      throw new RuntimeException(e);
    }
  }
}
