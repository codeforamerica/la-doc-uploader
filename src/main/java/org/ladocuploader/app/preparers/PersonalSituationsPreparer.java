package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PersonalSituationsPreparer implements SubmissionFieldPreparer {

//  you[]

  static String PERSONAL_SITUATION_STRING = "personalSituations_wildcard_";

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {

    List<String> personalSituations = (List<String>) submission.getInputData().getOrDefault(PERSONAL_SITUATION_STRING + "you[]", Collections.EMPTY_LIST);
    boolean disabilityFlag = personalSituations.contains("personal issue or disability");
    Map<String, SubmissionField> results = new HashMap<>();

    var household = (List<Map<String, Object>>) submission.getInputData().get("household");
    if (!disabilityFlag && household != null) {
      for (Map<String, Object> member : household) {
        var uuid = member.get("uuid");
        List<String> hhmemberSituations = (List<String>) submission.getInputData().getOrDefault(PERSONAL_SITUATION_STRING + uuid + "[]", Collections.EMPTY_LIST);
        if (hhmemberSituations.contains("personal issue or disability")) {
          results.put("personalSituationDisabilityInd", new SingleField("personalSituationDisabilityInd", "true", null));
          return results;
        }
      }
    }

    results.put("personalSituationDisabilityInd", new SingleField("personalSituationDisabilityInd", disabilityFlag ? "true" : "false", null));
    return results;
  }
}
