package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.stereotype.Component;

import java.util.*;

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
    Map<String, SubmissionField> results = new HashMap<>();

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
        encryptedSSN = (String) ((Map<?, ?>) householdMembers.get(i)).remove(ENCRYPTED_SSNS_INPUT_NAME);
        String decryptedSSN = getEncryptor().decrypt(encryptedSSN);
        householdSsnInputs.add(decryptedSSN);
      }
    }

    if (!householdSsnInputs.isEmpty()) {
      results.put("applicantSsn", new SingleField("applicantSsn", householdSsnInputs.get(0), null));

      for (int i = 1; i < householdSsnInputs.size(); i++) {
        results.put("ssns_" + i, new SingleField("ssns_" + i, householdSsnInputs.get(i), null));
      }

      return results;
    }

    return Collections.emptyMap();
  }
}
