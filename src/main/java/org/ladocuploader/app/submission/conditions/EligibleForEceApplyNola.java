package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

import static org.ladocuploader.app.submission.actions.SetExperimentGroups.ExperimentGroup.APPLY;
import static org.ladocuploader.app.utils.SubmissionUtilities.*;

@Component
public class EligibleForEceApplyNola implements Condition {

  @Override
  public Boolean run(Submission submission) {
    return isOrleansParish(submission)
        && inExperimentGroup(APPLY.name(), submission)
        && (hasChildBornAfterCutoff(submission, ECE_CUTOFF_DATE) || hasHouseholdPregnancy(submission));
  }
}
