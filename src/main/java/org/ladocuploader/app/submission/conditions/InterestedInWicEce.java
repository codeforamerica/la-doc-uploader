package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class InterestedInWicEce implements Condition {

  private final InNolaParish inNola;
  private final InterestedInECE interestedInECE;
  private final InterestedInWIC interestedInWIC;

  public InterestedInWicEce(InNolaParish inNola, InterestedInECE interestedInECE, InterestedInWIC interestedInWIC) {
    this.inNola = inNola;
    this.interestedInECE = interestedInECE;
    this.interestedInWIC = interestedInWIC;
  }

  @Override
  public Boolean run(Submission submission) {
    return inNola.run(submission) && (interestedInECE.run(submission) || interestedInWIC.run(submission));
  }
}
