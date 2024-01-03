package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class IsDocUploadExpired implements Condition {

    public Long expiryHours = 2L;


    @Override
    public Boolean run(Submission submission){
        OffsetDateTime submittedAt = submission.getSubmittedAt();
        OffsetDateTime now = OffsetDateTime.now();

        if (submittedAt != null){
            OffsetDateTime expiryTime = submittedAt.plusHours(expiryHours);

            return expiryTime.isAfter(now);
        }
        return false;
    }
}
