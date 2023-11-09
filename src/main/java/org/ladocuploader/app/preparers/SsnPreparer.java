package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class SsnPreparer implements SubmissionFieldPreparer {
  private static final String ENCRYPTED_SSNS_INPUT_NAME = "encryptedSSNs";

  private final StringEncryptor encryptor;

  public SsnPreparer(StringEncryptor stringEncryptor) {
    encryptor = stringEncryptor;
  }

  private StringEncryptor getEncryptor() {
    return encryptor;
  }

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    // Digital Assister
    List<String> householdSsnInputs = new ArrayList<>();
    String encryptedSSN = (String) submission.getInputData().remove(ENCRYPTED_SSNS_INPUT_NAME);
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
        encryptedSSN = (String) ((Map) householdMembers.get(i)).remove(ENCRYPTED_SSNS_INPUT_NAME);
        if (encryptedSSN != null) {
          String decryptedSSN = getEncryptor().decrypt(encryptedSSN);
          householdSsnInputs.add(decryptedSSN);
        } else {
          householdSsnInputs.add("");
        }
      }
    }

    if (!householdSsnInputs.isEmpty()) {
      return Map.of("applicantSsn", new SingleField("applicantSsn", householdSsnInputs.get(0), null));
    }

    return Collections.emptyMap();
  }
}
