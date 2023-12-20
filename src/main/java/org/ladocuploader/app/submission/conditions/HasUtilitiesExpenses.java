package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class HasUtilitiesExpenses extends BasicArrayCondition {

    @Override
    public Boolean run(Submission submission) {
        return run(submission, "householdUtilitiesExpenses[]");
    }
}
