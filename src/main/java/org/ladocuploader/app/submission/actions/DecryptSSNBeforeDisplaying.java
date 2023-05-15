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

  public DecryptSSNBeforeDisplaying() {
    encryptor = new StringEncryptor(System.getenv("ENCRYPTION_KEYSET"));
  }

  @Override
  public void run(Submission submission) {
    String encryptedSSN = (String) submission.getInputData().remove("encryptedSSN");
    if (encryptedSSN != null) {
      String decryptedSSN = encryptor.decrypt(encryptedSSN);
      submission.getInputData().put("ssn", decryptedSSN);
    }
  }
}
