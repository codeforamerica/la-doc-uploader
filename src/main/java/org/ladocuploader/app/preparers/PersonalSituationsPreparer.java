package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PersonalSituationsPreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    List<String> disability = (List<String>) submission.getInputData().get("personalSituationDisability[]");
    boolean disabilityFlag = disability != null && disability.contains("you");

    var household = (List<Map<String, Object>>) submission.getInputData().get("household");
    if (household != null && disability != null) {
      for (Map<String, Object> member : household) {
        var uuid = member.get("uuid");
        disabilityFlag = disabilityFlag || disability.contains(uuid);
      }
    }

    return Map.of("personalSituationDisablityInd", new SingleField("disablityInd", disabilityFlag ? "Yes" : "No", null));
  }
}
