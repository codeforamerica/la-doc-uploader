package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import java.util.Random;
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
    Random rand = new Random();
    int group = rand.nextInt(3);
    switch(group) {
      case 0 -> submission.getInputData().put("experimentGroup", ExperimentGroup.CONTROL);
      case 1 -> submission.getInputData().put("experimentGroup", ExperimentGroup.LINK);
      case 2 -> submission.getInputData().put("experimentGroup", ExperimentGroup.APPLY);
    }
  }
}
