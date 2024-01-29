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
  private final String REMINDER_METHOD_INPUT_NAME = "wantsReminders";
  private final String PHONE_NUMBER_INPUT_NAME = "phoneNumber";
  private final String CELL_NUMBER_INPUT_NAME = "cellPhoneNumber";
  private final String WORK_NUMBER_INPUT_NAME = "workPhoneNumber";

  @Override
  public Map<String, List<String>> runValidation(FormSubmission formSubmission, Submission submission) {
    Map<String, List<String>> errorMessages = new HashMap<>();
    Map<String, Object> inputData = formSubmission.getFormData();
    String reminderMethod = inputData.get(REMINDER_METHOD_INPUT_NAME+"[]").toString();
    String phoneNumber =(String) inputData.get(PHONE_NUMBER_INPUT_NAME);
    String cellphoneNumber =(String) inputData.get(CELL_NUMBER_INPUT_NAME);
    String workphoneNumber =(String) inputData.get(WORK_NUMBER_INPUT_NAME);

    if(reminderMethod.equals("[true]")){
      if(!Pattern.matches(PHONE_REGEX_PATTERN, phoneNumber) && !Pattern.matches(PHONE_REGEX_PATTERN, cellphoneNumber) && !Pattern.matches(PHONE_REGEX_PATTERN, workphoneNumber)){
          errorMessages.put(PHONE_NUMBER_INPUT_NAME, List.of(translateMessage("error.invalid-phone")));
      };
    }

    return errorMessages;
  }

}