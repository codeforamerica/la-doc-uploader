package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PersonalSituationsPreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    List<String> disability = (List<String>) submission.getInputData().get("personalSituationDisability[]");

    var household = (List<Map<String, Object>>) submission.getInputData().get("household");
    if (household != null) {
      for (Map<String, Object> member : household) {
        var uuid = member.get("uuid");

        String disabilityFlag = disability.contains(uuid) || disability.contains("you") ? "Yes" : "No";
        results.put("personalSituationDisablityInd", new SingleField("disablityInd", disabilityFlag, null));
      }
    }

    return results;
  }
}
