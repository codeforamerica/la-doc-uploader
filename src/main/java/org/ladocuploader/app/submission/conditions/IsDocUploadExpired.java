package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.ladocuploader.app.utils.SubmissionUtilities;
import org.springframework.stereotype.Component;

@Component
public class IsDocUploadExpired implements Condition {


    @Override
    public Boolean run(Submission submission){
        return SubmissionUtilities.isDocUploadExpired(submission);
    }
}
