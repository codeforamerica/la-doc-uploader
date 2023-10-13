package org.ladocuploader.app.submission.conditions;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class JobPaidByTheHour extends AbstractSubflowCondition {
    public Boolean run(Submission submission, String uuid) {
        var item = currentIncomeSubflowItem(submission, uuid);

        return item != null &&
                item.getOrDefault("jobPaidByHour", "false").equals("true");
    }
}
