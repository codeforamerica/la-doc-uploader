package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.FormSubmission;
import java.util.HashMap;
import java.util.Map;
import org.ladocuploader.app.submission.MixpanelTracker;

public abstract class TrackClientInfo implements Action {

  private final MixpanelTracker mixpanelTracker;

  public TrackClientInfo(MixpanelTracker mixpanelTracker) {
    this.mixpanelTracker = mixpanelTracker;
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

    properties.put("first_name", formData.containsKey("firstName"));
    properties.put("last_name", formData.containsKey("lastName"));
    properties.put("birth_date", formData.containsKey("birthDate"));
    properties.put("ssn", formData.containsKey("ssn"));
    properties.put("phone", formData.containsKey("phoneNumber"));
    properties.put("email", formData.containsKey("emailAddress"));
    properties.put("case_number", formData.containsKey("caseNumber"));

    mixpanelTracker.trackWithProfile(formData.get("submissionId"), getEventName(), properties);
  }

  abstract String getEventName();
}
