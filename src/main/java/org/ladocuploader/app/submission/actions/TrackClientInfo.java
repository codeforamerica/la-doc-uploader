package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.FormSubmission;
import formflow.library.data.Submission;
import org.ladocuploader.app.submission.MixpanelTracker;

import java.util.HashMap;
import java.util.Map;

public abstract class TrackClientInfo implements Action {

  private final MixpanelTracker mixpanelTracker;

  public TrackClientInfo(MixpanelTracker mixpanelTracker) {
    this.mixpanelTracker = mixpanelTracker;
  }

  @Override
  public void run(Submission submission) {
    Map<String, Object> inputData = submission.getInputData();
    trackProperties(inputData, submission.getId().toString());
  }

  @Override
  public void run(FormSubmission formSubmission) {
    Map<String, Object> inputData = formSubmission.getFormData();
    trackProperties(inputData, (String) inputData.get("submissionId"));
  }

  private void trackProperties(Map<String, Object> inputData, String submissionId) {
    Map<String, String> formData = new HashMap<>();
    inputData.forEach((k, v) -> {
      String value = v.toString();
      if (!(value.length() == 0) && !value.equals("[]")) {
        formData.put(k, value);
      }
    });

    Map<String, Object> properties = new HashMap<>();
    properties.put("first_name", formData.containsKey("firstName"));
    properties.put("last_name", formData.containsKey("lastName"));
    properties.put("birth_date", formData.containsKey("birthDate"));
    properties.put("ssn", formData.containsKey("ssn"));
    properties.put("phone", formData.containsKey("phoneNumber"));
    properties.put("email", formData.containsKey("emailAddress"));
    properties.put("case_number", formData.containsKey("caseNumber"));

    mixpanelTracker.trackWithProfile(submissionId, getEventName(), properties);
  }

  abstract String getEventName();
}
