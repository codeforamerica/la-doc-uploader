package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.data.TransmissionRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class HandleApplicationSigned implements Action {
  @Autowired
  private TransmissionRepositoryService transmissionRepositoryService;

  public void run(Submission submission) {
    if (transmissionRepositoryService.transmissionExists(submission)) {
      // already submitted. don't do anything again.
      return;
    }

    transmissionRepositoryService.createTransmissionRecord(submission);

  }
}