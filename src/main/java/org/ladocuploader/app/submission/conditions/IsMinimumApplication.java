package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class IsMinimumApplication implements Condition {

  @Override
  public Boolean run(Submission submission) {
    return "false".equals(submission.getInputData().get("isApplyingForExpeditedSnap"));
  }
}
