package org.ladocuploader.app.submission.conditions;

import formflow.library.config.submission.Condition;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class IsDocUploadExpired implements Condition {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    public Long expiryHours = 2L;


    @Override
    public Boolean run(Submission submission){
        OffsetDateTime submittedAt = submission.getSubmittedAt();
        OffsetDateTime now = OffsetDateTime.now();

        if (submittedAt != null){
            String formattedSubmittedAt = submittedAt.format(DATE_FORMAT);
            OffsetDateTime expiryTime = submittedAt.plusHours(expiryHours);

            return expiryTime.isAfter(now);
        }
        return false;
    }
}
