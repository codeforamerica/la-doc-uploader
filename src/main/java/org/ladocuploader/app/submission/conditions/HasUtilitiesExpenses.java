package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class HasUtilitiesExpenses implements Condition {

    @Override
    public Boolean run(Submission submission) {
        var inputData = submission.getInputData();
        if (inputData.containsKey("householdUtilitiesExpenses[]")) {
            var utilitiesArray = (ArrayList) submission.getInputData().get("householdUtilitiesExpenses[]");
          return !utilitiesArray.isEmpty() && !utilitiesArray.contains("None");
        }
        return false;
    }
}
