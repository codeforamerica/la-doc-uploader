package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class IsNotApplyingForExpeditedSnap implements Condition {

  @Override
  public Boolean run(Submission submission) {
      // They never saw the expedited snap question and didn't answer it
      return submission.getInputData().get("isApplyingForExpeditedSnap") == null;
  }
}
