package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class InterestedInECE implements Condition {

    @Override
    public Boolean run(Submission submission) {
        var interestedInEce = submission.getInputData().get("interestedInEceInd");
        return "true".equals(interestedInEce);
    }
}
