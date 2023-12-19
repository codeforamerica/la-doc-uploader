package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;
import static org.ladocuploader.app.utils.SubmissionUtilities.isEligibleForExperiment;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

@Component
public class SetExperimentGroups implements Action {

  public enum ExperimentGroup {
    CONTROL,
    LINK,
    APPLY
  }

  @Override
  public void run(Submission submission) {
    if (isEligibleForExperiment(submission)) {
      Random rand = new Random();
      int group = rand.nextInt(3);
      switch (group) {
        case 0 -> submission.getInputData().put("experimentGroup", ExperimentGroup.CONTROL);
        case 1 -> submission.getInputData().put("experimentGroup", ExperimentGroup.LINK);
        case 2 -> submission.getInputData().put("experimentGroup", ExperimentGroup.APPLY);
      }
    }
  }

}
