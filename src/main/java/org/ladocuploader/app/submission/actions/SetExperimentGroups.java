package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;


@Component
public class SetExperimentGroups implements Action {

  public enum ExperimentGroup {
    CONTROL,
    LINK,
    APPLY
  }

  @Override
  public void run(Submission submission) {
    submission.getInputData().put("experimentGroup", ExperimentGroup.APPLY);
  }

}
