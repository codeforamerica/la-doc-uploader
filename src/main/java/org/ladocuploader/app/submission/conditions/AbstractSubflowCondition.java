package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;

import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractSubflowCondition implements Condition {

    protected Map<String, Object> currentSubflowItem(Submission submission, String subflow, String uuid) {
        var inputData = submission.getInputData();
        var items = (ArrayList<Map<String, Object>>) inputData.getOrDefault(subflow, new ArrayList<Map<String, Object>>());
        return items
                .stream()
                .filter(item -> item.get("uuid").equals(uuid))
                .findFirst()
                .orElse(null);
    }

    protected Map<String, Object> currentIncomeSubflowItem(Submission submission, String uuid) {
        return currentSubflowItem(submission, "income", uuid);
    }
}
