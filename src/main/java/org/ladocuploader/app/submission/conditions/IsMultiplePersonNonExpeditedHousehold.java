package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class IsMultiplePersonNonExpeditedHousehold implements Condition {

  @Override
  public Boolean run(Submission submission) {
    var inputData = submission.getInputData();
    boolean isNotApplyingForExpeditedSnap = !Boolean.parseBoolean(
        (String) inputData.getOrDefault("isApplyingForExpeditedSnap", "false"));
    
    boolean isAMultiPersonHousehold = Boolean.parseBoolean(
        (String) inputData.getOrDefault("multiplePersonHousehold", "false"));

    return isAMultiPersonHousehold && isNotApplyingForExpeditedSnap;
  }
}
