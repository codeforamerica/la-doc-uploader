package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class ExpensesIncludeFloodInsurance implements Condition {

    @Override
    public Boolean run(Submission submission) {
        var inputData = submission.getInputData();
        if (inputData.containsKey("householdHomeExpenses[]")) {
            var programArr = (ArrayList<String>) submission.getInputData().get("householdHomeExpenses[]");
            return programArr.contains("Flood Insurance");
        }
        return false;
    }
}
