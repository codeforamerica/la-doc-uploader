package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import java.util.ArrayList;

public class BasicArrayCondition implements Condition {

    /**
     * Return true if the array corresponding to the key exists and ( is not empty or does not have "None" as its value)
     */
    @Override
    public Boolean run(Submission submission, String key) {
        var inputData = submission.getInputData();
        if (inputData.containsKey(key)){
            var array = (ArrayList) submission.getInputData().get(key);
            if(array.isEmpty()){
                return false;
            }
            return !array.contains("None");
        }
        return false;
    }
}
