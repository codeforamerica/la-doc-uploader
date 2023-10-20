package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import formflow.library.data.UserFileRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssociateDocTypeLabel implements Action {

    @Autowired
    UserFileRepositoryService userFileRepositoryService;

    @Override
    public void run(Submission submission){

    }
}
