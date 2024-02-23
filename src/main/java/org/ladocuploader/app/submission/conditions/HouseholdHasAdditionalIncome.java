package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class HouseholdHasAdditionalIncome implements Condition {
    @Override
    public Boolean run(Submission submission) {
        var inputData = submission.getInputData();
        if (inputData.containsKey("additionalIncome[]")) {
            var additionalIncomeArray = (ArrayList) submission.getInputData().get("additionalIncome[]");
            return !additionalIncomeArray.isEmpty() && !additionalIncomeArray.contains("None");
        }
        return false;
    }
}
