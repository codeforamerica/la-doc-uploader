package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.stereotype.Component;
import static formflow.library.inputs.FieldNameMarkers.DYNAMIC_FIELD_MARKER;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
public class DecryptSSNBeforeDisplaying implements Action {
  private static final String ENCRYPTED_SSNS_INPUT_NAME = "encryptedSSN";

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
    
    ArrayList<LinkedHashMap> householdMembers = (ArrayList) submission.getInputData().get("household");

    for (LinkedHashMap hhmember : householdMembers) {
      String ssnKey = "householdMemberSsn" + DYNAMIC_FIELD_MARKER + hhmember.get("uuid");
      encryptedSSN = (String) hhmember.remove(ENCRYPTED_SSNS_INPUT_NAME);
      if(!encryptedSSN.isBlank()) {
        String decryptedSSN = getEncryptor().decrypt(encryptedSSN);
        submission.getInputData().put(ssnKey, decryptedSSN);
      } else {
        submission.getInputData().put(ssnKey, "");
      }
    }
  }
}
