package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class ApplicantIsNotSelf implements Condition {

    @Override
    public Boolean run(Submission submission) {
        var inputData = submission.getInputData();
        if (inputData.containsKey("whosApplying")) {
            return !submission.getInputData().get("whosApplying").equals("Self");
        }
        return false;
    }
}
