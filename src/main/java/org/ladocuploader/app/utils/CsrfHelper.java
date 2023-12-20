package org.ladocuploader.app.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
public class CsrfHelper {

  //@Value("${form-flow.csrf-key:12345}")
  //String csrfKey;

  public static String generateCsrf(UUID submissionId, String csrfKey) {
    try {
      SecureRandom secureRandom = SecureRandom.getInstance("NativePRNG");
      String randomValue = String.valueOf(secureRandom.nextInt());

      return generateCsrf(submissionId, randomValue, csrfKey);
    } catch (NoSuchAlgorithmException e) {
      log.error("Unable to generate CSRF. Exception occurred: {}", e.getMessage()) ;
      return null;
    }
  }

  public static String generateCsrf(UUID submissionId, String randomValue, String csrfKey) {

    HMac hmac = new HMac(new SHA256Digest());
    hmac.init(new KeyParameter(csrfKey.getBytes()));

    String message = submissionId + "!" + randomValue;
    byte[] result = new byte[hmac.getMacSize()];
    byte[] bytes = message.getBytes();
    hmac.update(bytes, 0, bytes.length);
    hmac.doFinal(result, 0);

    return Hex.toHexString(result) + "." + message;
  }
}