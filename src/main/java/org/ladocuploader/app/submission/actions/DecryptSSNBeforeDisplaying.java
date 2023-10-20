package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
public class DecryptSSNBeforeDisplaying implements Action {
  private static final String ENCRYPTED_SSNS_INPUT_NAME = "encryptedSSNs";

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

    // Digital Assister
    List<String> householdSsnInputs = new ArrayList<>();
    encryptedSSN = (String) submission.getInputData().remove("encryptedSSNs");
    if (encryptedSSN != null) {
      String decryptedSSN = getEncryptor().decrypt(encryptedSSN);
      householdSsnInputs.add(decryptedSSN);
    }

    var householdMembers = (List) submission.getInputData().get("household");
    if (householdMembers != null && !householdMembers.isEmpty()) {
      if (householdSsnInputs.isEmpty()) {
        // Make sure there's one for head of household
        householdSsnInputs.add("");
      }
      for (int i = 0; i < householdMembers.size(); i++) {
        encryptedSSN = (String) ((Map) householdMembers.get(i)).remove("encryptedSSNs");
        if (encryptedSSN != null) {
          String decryptedSSN = getEncryptor().decrypt(encryptedSSN);
          householdSsnInputs.add(decryptedSSN);
        } else {
          householdSsnInputs.add("");
        }
      }
    }

    if (!householdSsnInputs.isEmpty()) {
      submission.getInputData().put("ssns", householdSsnInputs);
    }
  }
}
