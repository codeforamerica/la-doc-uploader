package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class SetLiheapInd implements Action {
    @Override
    public void run(Submission submission) {

        Map<String, Object> inputData = submission.getInputData();
        if (inputData.containsKey("receivesEnergyAssistance") &&
                inputData.get("receivesEnergyAssistance").toString().equals("false")) {
            submission.getInputData().put("assistanceThroughLiheap", "false");

        }
    }
}
