package org.ladocuploader.app.submission.actions;

import formflow.library.data.FormSubmission;
import formflow.library.data.Submission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ValidateBirthdate extends VerifyDate {
  private final String INPUT_NAME = "birthDay";

  @Override
  public Map<String, List<String>> runValidation(FormSubmission formSubmission, Submission submission) {
    Map<String, List<String>> errorMessages = new HashMap<>();
    Map<String, Object> inputData = formSubmission.getFormData();
    String day = (String) inputData.get("birthDay");
    String month = (String) inputData.get("birthMonth");
    String year = (String) inputData.get("birthYear");

    if (!Pattern.matches("\\d{4}",year)) {
      errorMessages.put(INPUT_NAME, List.of("Make sure to provide a date in MM/DD/YYYY OR M/D/YYYY."));
    } else if(!this.validRange(month+"/"+day+"/"+year)){
      errorMessages.put(INPUT_NAME, List.of("Make sure to provide a valid date."));
    }

    return errorMessages;
  }
}
