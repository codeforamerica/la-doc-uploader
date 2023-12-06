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

@Component
public class EncryptSSNBeforeSaving implements Action {
  private static final String ENCRYPTED_SSNS_INPUT_NAME = "encryptedSSN";

  private final StringEncryptor encryptor;

  public EncryptSSNBeforeSaving(StringEncryptor stringEncryptor) {
    encryptor = stringEncryptor;
  }

  @Override
  public void run(Submission submission) {
    String ssnInput = (String) submission.getInputData().remove("ssn");
    if (ssnInput != null) {
      String encryptedSSN = encryptor.encrypt(ssnInput);
      submission.getInputData().put("encryptedSSN", encryptedSSN);
    }

    ArrayList<LinkedHashMap> householdMembers = (ArrayList) submission.getInputData().get("household");

    for (LinkedHashMap hhmember : householdMembers) {
      String ssnKey = "householdMemberSsn" + DYNAMIC_FIELD_MARKER + hhmember.get("uuid");
        var householdMemberSsn = (String) submission.getInputData().remove(ssnKey);
        if(!householdMemberSsn.isBlank()){
          String encryptedSSN = encryptor.encrypt(householdMemberSsn);
          hhmember.put(ENCRYPTED_SSNS_INPUT_NAME,encryptedSSN);
        } else {
          hhmember.put(ENCRYPTED_SSNS_INPUT_NAME, "");
      }
    }
  }
}
