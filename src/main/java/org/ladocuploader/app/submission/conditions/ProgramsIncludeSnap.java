package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class ProgramsIncludeSnap implements Condition {

    public Boolean run(Submission submission) {
        var inputData = submission.getInputData();
        if (inputData.containsKey("programs[]")) {
            var programArr = (ArrayList<String>) submission.getInputData().get("programs[]");
            return programArr.contains("SNAP");
        }
        return false;
    }
}
