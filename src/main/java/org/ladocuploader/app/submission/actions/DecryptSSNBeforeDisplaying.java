package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.ladocuploader.app.utils.SubmissionUtilities.ENCRYPTED_SSNS_INPUT_NAME;
import static org.ladocuploader.app.utils.SubmissionUtilities.getDecryptedSSNKeyName;

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

  @Override
  public void run(Submission submission) {
    String encryptedSSN = (String) submission.getInputData().remove("encryptedSSN");
    if (encryptedSSN != null) {
      String decryptedSSN = getEncryptor().decrypt(encryptedSSN);
      submission.getInputData().put("ssn", decryptedSSN);
    }

    List<Map<String, Object>> householdMembers = (List) submission.getInputData().getOrDefault("household", emptyList());
    for (var hhmember : householdMembers) {
      String ssnKey = getDecryptedSSNKeyName((String) hhmember.get("uuid"));
      encryptedSSN = (String) hhmember.remove(ENCRYPTED_SSNS_INPUT_NAME);
      String decryptedSSN = getEncryptor().decrypt(encryptedSSN);
      submission.getInputData().put(ssnKey, decryptedSSN);
    }
  }
}
