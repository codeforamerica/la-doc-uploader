package org.ladocuploader.app.submission.actions;

import formflow.library.data.FormSubmission;
import formflow.library.data.Submission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ValidateContactMethod extends AssisterAction {
  private final String REMINDER_METHOD_INPUT_NAME = "remindersMethod";
  private final String PHONE_NUMBER_INPUT_NAME = "phoneNumber";
  private final String EMAIL_ADDRESS_INPUT_NAME = "emailAddress";
  @Override
  public Map<String, List<String>> runValidation(FormSubmission formSubmission, Submission submission) {
    Map<String, List<String>> errorMessages = new HashMap<>();
    Map<String, Object> inputData = formSubmission.getFormData();
    ArrayList<String> reminderMethod = (ArrayList) inputData.get(REMINDER_METHOD_INPUT_NAME +"[]");
    String phoneNumber =(String) inputData.get(PHONE_NUMBER_INPUT_NAME);
    String emailAddress = (String) inputData.get(EMAIL_ADDRESS_INPUT_NAME);

    if(reminderMethod.contains("By Text") || !phoneNumber.isEmpty()){
      if(!Pattern.matches(PHONE_REGEX_PATTERN, phoneNumber)){
        errorMessages.put(PHONE_NUMBER_INPUT_NAME, List.of(translateMessage("error.invalid-phone")));
      };
    }
    if(reminderMethod.contains("By Email") || !emailAddress.isEmpty()){
      if(!Pattern.matches(EMAIL_REGEX_PATTERN, emailAddress)){
        errorMessages.put(EMAIL_ADDRESS_INPUT_NAME, List.of(translateMessage("error.invalid-email")));
      };
    }

    return errorMessages;
  }

}