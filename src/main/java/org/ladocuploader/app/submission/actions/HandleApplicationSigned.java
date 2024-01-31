package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.data.TransmissionRepositoryService;
import org.ladocuploader.app.data.enums.TransmissionType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.ladocuploader.app.submission.actions.SetExperimentGroups.ExperimentGroup.APPLY;
import static org.ladocuploader.app.utils.SubmissionUtilities.inExperimentGroup;


@Slf4j
@Component
public class HandleApplicationSigned implements Action {

  private final SubmissionRepositoryService submissionRepositoryService;
  private final TransmissionRepositoryService transmissionRepositoryService;

  public HandleApplicationSigned(SubmissionRepositoryService submissionRepositoryService, TransmissionRepositoryService transmissionRepositoryService) {
    this.submissionRepositoryService = submissionRepositoryService;
    this.transmissionRepositoryService = transmissionRepositoryService;
  }

  @Override
  public void run(Submission submission) {
    // Clean up submission data
    removeIncompleteIterations(submission, "household");
    removeIncompleteIterations(submission, "income");
    submissionRepositoryService.save(submission);

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

    if(!transmissionRepositoryService.transmissionExists(submission, TransmissionType.SNAP)) {
      transmissionRepositoryService.createTransmissionRecord(submission, TransmissionType.SNAP);
    }
  }

  private static void removeIncompleteIterations(Submission submission, String subflowName) {
    var subflowdata = (List<Map<String, Object>>) submission.getInputData().getOrDefault(subflowName, emptyList());
    if (!subflowdata.isEmpty()) {
      var newsubflowdata = new ArrayList<Map<String, Object>>();
      for(var item : subflowdata) {
        boolean iterationIsComplete = (boolean) item.getOrDefault("iterationIsComplete", false);
        if (iterationIsComplete) {
          newsubflowdata.add(item);
        }
      }
      submission.getInputData().put(subflowName, newsubflowdata);
    }
  }
}