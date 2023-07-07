package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class DecryptSSNBeforeDisplaying implements Action {

  private StringEncryptor getEncryptor(byte[] iv) {
    return new StringEncryptor(System.getenv("ENCRYPTION_KEY"), iv);
  }

  public void run(Submission submission) {
    String encryptedSSN = (String) submission.getInputData().remove("encryptedSSN");
    byte[] encryptedSSN_iv = (byte[]) submission.getInputData().remove("encryptedSSN_iv");
    if (encryptedSSN != null) {
      String decryptedSSN = getEncryptor(encryptedSSN_iv).decrypt(encryptedSSN);
      submission.getInputData().put("ssn", decryptedSSN);
    }
  }
}
