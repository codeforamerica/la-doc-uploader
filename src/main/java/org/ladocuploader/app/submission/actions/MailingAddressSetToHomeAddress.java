package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Slf4j
public class MailingAddressSetToHomeAddress implements Action {
  @Override
  public void run(Submission submission) {
    Map<String, Object> inputData = submission.getInputData();

    if(submission.getInputData().get("sameAsHomeAddress[]").toString().equals("[true]")){
      submission.getInputData().put("mailingAddressZipCode", inputData.get("homeAddressZipCode"));
      submission.getInputData().put("mailingAddressState", inputData.get("homeAddressState"));
      submission.getInputData().put("mailingAddressCity", inputData.get("homeAddressCity"));
      submission.getInputData().put("mailingAddressStreetAddress1", inputData.get("homeAddressStreetAddress1"));
      submission.getInputData().put("mailingAddressStreetAddress2", inputData.get("homeAddressStreetAddress2"));
    }
  }
}