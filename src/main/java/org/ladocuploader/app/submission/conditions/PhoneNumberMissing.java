package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberMissing implements Condition {

    @Override
    public Boolean run(Submission submission) {
        var inputData = submission.getInputData();
        var anyNumber = true;
        if (inputData.containsKey("phoneNumber") && !submission.getInputData().get("phoneNumber").equals("")) {
            anyNumber = false;
        }
        if (inputData.containsKey("cellPhoneNumber") && !submission.getInputData().get("cellPhoneNumber").equals("")) {
            anyNumber = false;
        }
        if (inputData.containsKey("workPhoneNumber") && !submission.getInputData().get("workPhoneNumber").equals("")) {
            anyNumber = false;
        }
        return anyNumber;
    }
}