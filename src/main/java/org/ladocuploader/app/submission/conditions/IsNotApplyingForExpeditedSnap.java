package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class IsNotApplyingForExpeditedSnap implements Condition {
    
    @Override
    public Boolean run(Submission submission) {
        var inputData = submission.getInputData();
        if (inputData.containsKey("isApplyingForExpeditedSnap")) {
            return inputData.get("isApplyingForExpeditedSnap").equals("false");
        }
        // They never saw the expedited snap question, so they're not applying for it
        return false;
    }
}
