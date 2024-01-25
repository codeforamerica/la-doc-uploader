package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

import static org.ladocuploader.app.utils.SubmissionUtilities.isOrleansParish;

@Component
public class InterestedInWicEce implements Condition {

  private final InterestedInECE interestedInECE;
  private final InterestedInWIC interestedInWIC;

  public InterestedInWicEce(InterestedInECE interestedInECE, InterestedInWIC interestedInWIC) {
    this.interestedInECE = interestedInECE;
    this.interestedInWIC = interestedInWIC;
  }

  @Override
  public Boolean run(Submission submission) {
    return isOrleansParish(submission) && (interestedInECE.run(submission) || interestedInWIC.run(submission));
  }
}
