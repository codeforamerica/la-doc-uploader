package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.data.TransmissionRepositoryService;
import org.ladocuploader.app.data.enums.TransmissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.ladocuploader.app.submission.actions.SetExperimentGroups.ExperimentGroup.APPLY;
import static org.ladocuploader.app.utils.SubmissionUtilities.inExperimentGroup;


@Slf4j
@Component
public class HandleApplicationSigned implements Action {

  @Autowired
  private TransmissionRepositoryService transmissionRepositoryService;

  @Override
  public void run(Submission submission) {
    // Create WIC + ECE records if they don't exist
    if (!transmissionRepositoryService.transmissionExists(submission, TransmissionType.WIC)) {
      // TODO: should we also check if any questions were answered? Or is this enough?
      if (inExperimentGroup(APPLY.name(), submission)){
        transmissionRepositoryService.createTransmissionRecord(submission, TransmissionType.WIC);
      }

    }

    if (!transmissionRepositoryService.transmissionExists(submission, TransmissionType.ECE)) {
      if (inExperimentGroup(APPLY.name(), submission)) {
        transmissionRepositoryService.createTransmissionRecord(submission, TransmissionType.ECE);
      }
    }
  }
}