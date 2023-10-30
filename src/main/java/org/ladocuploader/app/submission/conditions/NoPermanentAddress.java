package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoPermanentAddress implements Condition {

  @Override
  public Boolean run(Submission submission) {
    var inputData = submission.getInputData();
    if (inputData.containsKey("noHomeAddress[]")) {
      List<String> selections = (List<String>) submission.getInputData().get("noHomeAddress[]");
      return !selections.isEmpty() && selections.get(0).equals("true");
    }
    return false;
  }
}