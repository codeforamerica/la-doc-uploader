package org.ladocuploader.app.submission.conditions;

import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class OutOfStateBenefits extends BasicCondition {

  @Override
  public Boolean run(Submission submission) {
    return run(submission, "outOfStateBenefitsInd", "true");
  }
}
