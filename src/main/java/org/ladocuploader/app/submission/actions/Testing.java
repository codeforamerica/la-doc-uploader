package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class Testing implements Action {

  @Override
  public void run(Submission submission) {
//    Check what field is being completed
//    Set the current field to the next one
//

    String ssnInput = (String) submission.getInputData().remove("ssn");

  }
}
