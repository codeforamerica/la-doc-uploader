package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class InterestedInWIC implements Condition {

  @Override
  public Boolean run(Submission submission) {
    var interestedInWic = submission.getInputData().get("interestedInWicInd");
    return "true".equals(interestedInWic);
  }
}
