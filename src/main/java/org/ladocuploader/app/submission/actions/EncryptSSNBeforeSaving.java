package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptSSNBeforeSaving implements Action {

  private final StringEncryptor encryptor;

  public EncryptSSNBeforeSaving(@Value("${form-flow.aws.cmk}") String keyArn, @Value("${form-flow.aws.access_key}") String accessKey,
                                    @Value("${form-flow.aws.secret_key}") String secretKey) {
    encryptor = new StringEncryptor(keyArn, accessKey, secretKey);
  }

  public void run(Submission submission) {
    String ssnInput = (String) submission.getInputData().remove("ssn");
    if (ssnInput != null) {
      String encryptedSSN = encryptor.encrypt(ssnInput);
      submission.getInputData().put("encryptedSSN", encryptedSSN);
    }
  }
}
