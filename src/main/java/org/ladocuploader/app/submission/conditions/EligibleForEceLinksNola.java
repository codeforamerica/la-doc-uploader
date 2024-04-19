package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

import static org.ladocuploader.app.submission.actions.SetExperimentGroups.ExperimentGroup.LINK;
import static org.ladocuploader.app.utils.SubmissionUtilities.*;

@Component
public class EligibleForEceLinksNola implements Condition {

  @Override
  public Boolean run(Submission submission) {
    return isOrleansParish(submission)
        && inExperimentGroup(LINK.name(), submission)
        && (hasChildBornAfterCutoff(submission, ECE_CUTOFF_DATE) || hasHouseholdPregnancy(submission));
  }
}
