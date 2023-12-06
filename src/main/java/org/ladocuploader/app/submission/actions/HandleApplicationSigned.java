package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.data.TransmissionRepositoryService;
import org.ladocuploader.app.data.enums.TransmissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class HandleApplicationSigned implements Action {
  @Autowired
  private TransmissionRepositoryService transmissionRepositoryService;
  public void run(Submission submission) {
    // Create WIC + ECE records if they don't exist
    if (!transmissionRepositoryService.transmissionExists(submission, TransmissionType.WIC)) {
      // already submitted WIC. don't do anything again.
      transmissionRepositoryService.createTransmissionRecord(submission, TransmissionType.WIC);
    }

    if (!transmissionRepositoryService.transmissionExists(submission, TransmissionType.ECE)) {
      // already submitted WIC. don't do anything again.
      transmissionRepositoryService.createTransmissionRecord(submission, TransmissionType.ECE);
    }



  }
}