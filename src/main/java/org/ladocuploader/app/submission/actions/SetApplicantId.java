package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 */
@Component
@Slf4j
public class SetApplicantId implements Action {

  @Override
  public void run(Submission submission) {
    Map<String, Object> inputData = submission.getInputData();
    var submissionId = submission.getId().toString();
    String applicantId = (String) inputData.getOrDefault("applicantId", "");

    if (applicantId.isBlank()) {
      inputData.put("applicantId", submissionId);
    }
  }


}
