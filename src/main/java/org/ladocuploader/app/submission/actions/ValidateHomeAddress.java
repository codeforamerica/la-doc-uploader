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
public class ValidateHomeAddress implements Action {
  private final String NO_HOME_ADDRESS_INPUT_NAME = "noHomeAddress";
  private final String ADDRESS_1_INPUT_NAME = "homeAddressStreetAddress1";
  private final String CITY_INPUT_NAME = "homeAddressCity";
  private final String STATE_INPUT_NAME = "homeAddressState";
  private final String ZIP_INPUT_NAME = "homeAddressZipCode";


  @Override
  public Map<String, List<String>> runValidation(FormSubmission formSubmission, Submission submission) {
    Map<String, List<String>> errorMessages = new HashMap<>();
    Map<String, Object> inputData = formSubmission.getFormData();
    String address1 = (String) inputData.get("homeAddressStreetAddress1");
    String city = (String) inputData.get("homeAddressCity");
    String state = (String) inputData.get("homeAddressState");
    String zip = (String) inputData.get("homeAddressZipCode");

    if(homeAddressExpected(inputData)){
      if((address1+" "+city+" "+state+" "+zip).isBlank()){
        errorMessages.put(NO_HOME_ADDRESS_INPUT_NAME, List.of("Make sure to provide a home address or select ‘I don’t have a permanent address’. "));
      } else {
        if(address1.isBlank()){
          errorMessages.put(ADDRESS_1_INPUT_NAME, List.of("Make sure you answer this question."));
        };
        if(city.isBlank()){
          errorMessages.put(CITY_INPUT_NAME, List.of("Make sure you answer this question."));
        };
        if(state.isBlank()){
          errorMessages.put(STATE_INPUT_NAME, List.of("Make sure you answer this question."));
        };
        if (!Pattern.matches("\\d{5}",zip)) {
          errorMessages.put(ZIP_INPUT_NAME, List.of("Make sure to enter a zip code with 5 digits."));
        }
        if(errorMessages.containsKey(NO_HOME_ADDRESS_INPUT_NAME)){
          errorMessages.remove(NO_HOME_ADDRESS_INPUT_NAME, List.of("Make sure to provide a home address or select 'I don’t have a permanent address'."));
        }
      }

    }

    return errorMessages;
  }

  protected boolean homeAddressExpected(Map<String, Object> inputData){
    if(inputData.containsKey("noHomeAddress[]")){
      ArrayList<String> noHomeAddress = (ArrayList) inputData.get("noHomeAddress[]");
      return noHomeAddress.isEmpty();
    }
    return true;
  }

}