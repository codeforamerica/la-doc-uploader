package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
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
public class ValidateMailingAddress implements Action {
  private final String ADDRESS_1_INPUT_NAME = "mailingAddressStreetAddress1";
  private final String CITY_INPUT_NAME = "mailingAddressCity";
  private final String STATE_INPUT_NAME = "mailingAddressState";
  private final String ZIP_INPUT_NAME = "mailingAddressZipCode";


  @Override
  public Map<String, List<String>> runValidation(FormSubmission formSubmission, Submission submission) {
    Map<String, List<String>> errorMessages = new HashMap<>();
    Map<String, Object> inputData = formSubmission.getFormData();
    boolean validateAgainstHomeAddressBoolean = validateAgainstHomeAddress(inputData);

    if(validateAgainstHomeAddressBoolean){
      if(homeAddressElementHasError(submission, "homeAddressStreetAddress1")){
        errorMessages.put(ADDRESS_1_INPUT_NAME, List.of("Make sure you answer this question."));
      }
      if(homeAddressElementHasError(submission, "homeAddressCity")){
        errorMessages.put(CITY_INPUT_NAME, List.of("Make sure you answer this question."));
      }
      if(homeAddressElementHasError(submission, "homeAddressState")){
        errorMessages.put(STATE_INPUT_NAME, List.of("Make sure you answer this question."));
      }
      if(homeAddressZipHasError(submission)){
        errorMessages.put(ZIP_INPUT_NAME, List.of("Make sure to enter a zip code with 5 digits."));
      }
    } else {
      if(mailingAddressHasError(inputData, ADDRESS_1_INPUT_NAME)){
        errorMessages.put(ADDRESS_1_INPUT_NAME, List.of("Make sure you answer this question."));
      }
      if(mailingAddressHasError(inputData, CITY_INPUT_NAME)){
        errorMessages.put(CITY_INPUT_NAME, List.of("Make sure you answer this question."));
      }
      if(mailingAddressHasError(inputData, STATE_INPUT_NAME)){
        errorMessages.put(STATE_INPUT_NAME, List.of("Make sure you answer this question."));
      }
      if(mailingAddressZipHasError(inputData)){
        errorMessages.put(ZIP_INPUT_NAME, List.of("Make sure to enter a zip code with 5 digits."));
      }
    }

    return errorMessages;
  }

  protected boolean validateAgainstHomeAddress(Map<String, Object> inputData){
    if(inputData.containsKey("sameAsHomeAddress[]")){
      ArrayList<String> sameAsHomeAddress = (ArrayList) inputData.get("sameAsHomeAddress[]");
      return sameAsHomeAddress.contains("true");
    }
    return false;
  }

  protected boolean homeAddressElementHasError(Submission submission, String inputKey){
    return !submission.getInputData().containsKey(inputKey);
  }

  protected boolean homeAddressZipHasError(Submission submission){
    if(submission.getInputData().containsKey("homeAddressZip")){
      return !Pattern.matches("\\d{5}", submission.getInputData().get("homeAddressZip").toString());
    } else{
      return submission.getInputData().containsKey("homeAddressZip");
    }
  }

  protected boolean mailingAddressHasError(Map<String, Object> inputData, String inputKey){
    return inputData.get(inputKey).toString().isBlank();
  }

  protected boolean mailingAddressZipHasError(Map<String, Object> inputData){
    if(inputData.containsKey(ZIP_INPUT_NAME)){
      return !Pattern.matches("\\d{5}", inputData.get(ZIP_INPUT_NAME).toString());
    } else{
      return inputData.containsKey(ZIP_INPUT_NAME);
    }
  }


}