package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class ApplicantRaceEthnicityQuestionsAllowed implements Condition {

    @Override
    public Boolean run(Submission submission) {
        var inputData = submission.getInputData();
        if (inputData.containsKey("permissionToAskAboutRace")) {
            return submission.getInputData().get("permissionToAskAboutRace").equals("true");
        }
        return false;
    }
}
