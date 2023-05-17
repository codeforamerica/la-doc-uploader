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
    Map<String, Object> properties = new HashMap<>();
    Map<String, String> formData = new HashMap<>();
    submission.getInputData().forEach((k, v) -> {
      String value = v.toString();
      if (!(value.length() == 0) && !value.equals("[]")) {
        formData.put(k, value);
      }
    });

    trackProperties(properties, formData, submission.getId().toString());
  }

  @Override
  public void run(FormSubmission formSubmission) {
    Map<String, Object> properties = new HashMap<>();
    Map<String, String> formData = new HashMap<>();
    formSubmission.getFormData().forEach((k, v) -> {
      String value = v.toString();
      if (!(value.length() == 0) && !value.equals("[]")) {
        formData.put(k, value);
      }
    });

    trackProperties(properties, formData, formData.get("submissionId"));
  }

  private void trackProperties(Map<String, Object> properties, Map<String, String> formData, String submissionId) {
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
