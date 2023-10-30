package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class ExpensesIncludeVisionInsurancePremium implements Condition {

    @Override
    public Boolean run(Submission submission) {
        var inputData = submission.getInputData();
        if (inputData.containsKey("householdInsuranceExpenses[]")) {
            var programArr = (ArrayList<String>) submission.getInputData().get("householdInsuranceExpenses[]");
            return programArr.contains("Vision insurance premiums");
        }
        return false;
    }
}
