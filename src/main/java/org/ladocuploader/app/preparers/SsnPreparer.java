package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.ladocuploader.app.submission.StringEncryptor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.ladocuploader.app.utils.SubmissionUtilities.ENCRYPTED_SSNS_INPUT_NAME;

@Component
public class SsnPreparer implements SubmissionFieldPreparer {
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
    String encryptedSSN = (String) submission.getInputData().remove("encryptedSSN");
    if (encryptedSSN != null) {
      String decryptedSSN = encryptor.decrypt(encryptedSSN);
      results.put("applicantSsn", new SingleField("applicantSsn", decryptedSSN, null));
    }

    List<Map<String, Object>> householdMembers = (List) submission.getInputData().get("household");
    if(householdMembers != null) {
      int i = 1;
      for (Map<String, Object> hhmember : householdMembers) {
        encryptedSSN = (String) hhmember.get(ENCRYPTED_SSNS_INPUT_NAME);
        String decryptedSSN = encryptor.decrypt(encryptedSSN);
        results.put("ssns_" + i, new SingleField("ssns_" + i, decryptedSSN, null));
        i++;
      }
    }

    return results;
  }
}
