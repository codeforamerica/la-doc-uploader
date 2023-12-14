package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.utils.HouseholdUtilities;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Slf4j
public class ReformatPersonalSituationUserData implements Action {

  @Override
  public void run(Submission submission) {
    submission.getInputData().put("affectedByPersonalSituations[]", HouseholdUtilities.formattedHouseholdData(submission, "personalSituationsHouseholdUUID[]"));
  }

}
