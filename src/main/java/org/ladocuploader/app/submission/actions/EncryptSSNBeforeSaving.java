package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.stereotype.Component;

@Component
public class EncryptSSNBeforeSaving implements Action {

  private final StringEncryptor encryptor;

  public EncryptSSNBeforeSaving() {
    encryptor = new StringEncryptor(System.getenv("ENCRYPTION_KEYSET"));
  }

  @Override
  public void run(Submission submission) {
    String ssnInput = (String) submission.getInputData().remove("ssn");
    if (ssnInput != null) {
      String encryptedSSN = encryptor.encrypt(ssnInput);
      submission.getInputData().put("encryptedSSN", encryptedSSN);
    }
  }
}
