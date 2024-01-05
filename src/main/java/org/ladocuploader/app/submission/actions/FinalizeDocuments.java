package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

@Component
public class FinalizeDocuments implements Action {
    @Override
    public void run(Submission submission) {
        submission.getInputData().put("docUploadFinalized", "true");
    }
}
