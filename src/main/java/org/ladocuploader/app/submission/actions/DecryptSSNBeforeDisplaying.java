package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.apache.tomcat.util.codec.binary.Base64;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class DecryptSSNBeforeDisplaying implements Action {

  private StringEncryptor getEncryptor(String iv) {
    return new StringEncryptor(System.getenv("ENCRYPTION_KEY"), Base64.decodeBase64(iv));
  }

  public void run(Submission submission) {
    String encryptedSSN = (String) submission.getInputData().remove("encryptedSSN");
    if (encryptedSSN != null) {
      String encryptedSSN_iv = (String) submission.getInputData().remove("encryptedSSN_iv");
      String decryptedSSN = getEncryptor(encryptedSSN_iv).decrypt(encryptedSSN);
      submission.getInputData().put("ssn", decryptedSSN);
    }
  }
}
