package org.ladocuploader.app.submission.conditions;

import static org.ladocuploader.app.submission.actions.SetExperimentGroups.ExperimentGroup.APPLY;
import static org.ladocuploader.app.utils.SubmissionUtilities.ECE_CUTOFF_DATE;
import static org.ladocuploader.app.utils.SubmissionUtilities.hasChildBornAfterCutoff;
import static org.ladocuploader.app.utils.SubmissionUtilities.hasHouseholdPregnancy;
import static org.ladocuploader.app.utils.SubmissionUtilities.inExperimentGroup;
import static org.ladocuploader.app.utils.SubmissionUtilities.isJeffersonParish;
import static org.ladocuploader.app.utils.SubmissionUtilities.isOrleansParish;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class EligibleForEceApplyJefferson implements Condition {

  @Override
  public Boolean run(Submission submission) {
    return isJeffersonParish(submission)
        && inExperimentGroup(APPLY.name(), submission)
        && (hasChildBornAfterCutoff(submission, ECE_CUTOFF_DATE) || hasHouseholdPregnancy(submission));
  }
}
