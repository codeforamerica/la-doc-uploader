package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

import static org.ladocuploader.app.submission.actions.SetExperimentGroups.ExperimentGroup.APPLY;
import static org.ladocuploader.app.utils.SubmissionUtilities.inExperimentGroup;
import static org.ladocuploader.app.utils.SubmissionUtilities.isNolaParish;

@Component
public class EligibleForOutsideNolaWicApply implements Condition {

  @Override
  public Boolean run(Submission submission) {
    return !isNolaParish(submission) && inExperimentGroup(APPLY.name(), submission);
  }
}
