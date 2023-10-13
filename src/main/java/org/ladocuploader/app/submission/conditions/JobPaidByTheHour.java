package org.ladocuploader.app.submission.conditions;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class JobPaidByTheHour extends BasicCondition {
    @Override
    public Boolean run(Submission submission) {
        return run(submission, "jobPaidByHour", "true");
    }
}
