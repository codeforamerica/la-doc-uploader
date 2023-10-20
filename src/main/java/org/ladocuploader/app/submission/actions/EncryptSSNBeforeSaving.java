package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EncryptSSNBeforeSaving implements Action {
  private static final String ENCRYPTED_SSNS_INPUT_NAME = "encryptedSSNs";

  private final StringEncryptor encryptor;

  public EncryptSSNBeforeSaving(StringEncryptor stringEncryptor) {
    encryptor = stringEncryptor;
  }

  @Override
  public void run(Submission submission) {
    String ssnInput = (String) submission.getInputData().remove("ssn");
    if (ssnInput != null && !ssnInput.isEmpty()) {
      String encryptedSSN = encryptor.encrypt(ssnInput);
      submission.getInputData().put("encryptedSSN", encryptedSSN);
    }

    var householdSsnInputs = submission.getInputData().remove("ssns");
    if (householdSsnInputs != null) {
      if (householdSsnInputs instanceof String && !((String) householdSsnInputs).isEmpty()) {
        String encryptedSSN = encryptor.encrypt((String) householdSsnInputs);
        submission.getInputData().put(ENCRYPTED_SSNS_INPUT_NAME, encryptedSSN);
      } else if (householdSsnInputs instanceof List && !((List) householdSsnInputs).isEmpty()) {
        var householdSSNs = ((List<String>) householdSsnInputs);
        String encryptedSSN = encryptor.encrypt(householdSSNs.get(0));
        submission.getInputData().put(ENCRYPTED_SSNS_INPUT_NAME, encryptedSSN);

        var householdMembers = (List) submission.getInputData().get("household");
        for (int i = 0; i < householdMembers.size(); i++) {
          encryptedSSN = encryptor.encrypt(householdSSNs.get(i + 1));
          ((Map) householdMembers.get(i)).put(ENCRYPTED_SSNS_INPUT_NAME, encryptedSSN);
        }
      }
    }
  }
}
