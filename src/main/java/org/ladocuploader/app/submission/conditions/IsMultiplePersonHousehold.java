package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class IsMultiplePersonHousehold implements Condition {
  @Override
  public Boolean run(Submission submission) {
    var inputData = submission.getInputData();
    if (inputData.containsKey("multiplePersonHousehold")) {
      return submission.getInputData().get("multiplePersonHousehold").equals("true");
    }
    return false;
  }
}
