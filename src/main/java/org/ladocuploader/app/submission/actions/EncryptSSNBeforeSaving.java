package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.ladocuploader.app.utils.SubmissionUtilities.ENCRYPTED_SSNS_INPUT_NAME;
import static org.ladocuploader.app.utils.SubmissionUtilities.getDecryptedSSNKeyName;

@Component
public class EncryptSSNBeforeSaving implements Action {

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
    if (householdMembers != null) {
      for (LinkedHashMap hhmember : householdMembers) {
        String ssnKey = getDecryptedSSNKeyName((String) hhmember.get("uuid"));
        var householdMemberSsn = (String) submission.getInputData().remove(ssnKey);
        String encryptedSSN = encryptor.encrypt(householdMemberSsn);
        hhmember.put(ENCRYPTED_SSNS_INPUT_NAME, encryptedSSN);
      }
    }
  }
}
