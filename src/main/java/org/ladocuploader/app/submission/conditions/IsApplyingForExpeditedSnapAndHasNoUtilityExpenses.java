package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IsApplyingForExpeditedSnapAndHasNoUtilityExpenses implements Condition {
    
    @Autowired
    private IsApplyingForExpeditedSnap isApplyingForExpeditedSnap;
    @Override
    public Boolean run(Submission submission) {
        var inputData = submission.getInputData();
        if (inputData.containsKey("householdUtilitiesExpenses[]")) {
            var utilitiesArray = (ArrayList) submission.getInputData().get("householdUtilitiesExpenses[]");
            return utilitiesArray.contains("None") && isApplyingForExpeditedSnap.run(submission);
        }
        return false;
    }
}
