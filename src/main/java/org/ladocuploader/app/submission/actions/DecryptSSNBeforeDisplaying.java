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

  private final StringEncryptor encryptor;

  public DecryptSSNBeforeDisplaying(StringEncryptor stringEncryptor) {
    encryptor = stringEncryptor;
  }

  private StringEncryptor getEncryptor() {
    return encryptor;
  }

  public void run(Submission submission) {
    String encryptedSSN = (String) submission.getInputData().remove("encryptedSSN");
    if (encryptedSSN != null) {
      String decryptedSSN = getEncryptor().decrypt(encryptedSSN);
      submission.getInputData().put("ssn", decryptedSSN);
    }
  }
}
