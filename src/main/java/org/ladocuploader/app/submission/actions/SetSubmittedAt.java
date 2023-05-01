package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class SetSubmittedAt implements Action {


  public void run(Submission submission) {

    Date date = new Date();
    Timestamp currentTimestamp = new Timestamp(date.getTime());

    submission.getInputData().put("submittedAt", currentTimestamp);
  }
}
