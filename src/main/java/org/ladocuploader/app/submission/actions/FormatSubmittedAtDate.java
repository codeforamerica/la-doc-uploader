package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
@Component
public class FormatSubmittedAtDate implements Action {
  public final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM d, yyyy");
  private static final long TEN_DAYS = (1000 * 60 * 60 * 24) * 10;

  @Override
  public void run(Submission submission) {
    Date submittedAt = submission.getSubmittedAt();

    if (submittedAt != null) {
      String formattedSubmittedAt = DATE_FORMAT.format(submittedAt);
      submission.getInputData().put("formattedSubmittedAt", formattedSubmittedAt);

      String interviewDate = DATE_FORMAT.format(new Date(submittedAt.getTime() + TEN_DAYS));
      submission.getInputData().put("interviewDate", interviewDate);
    }
  }
}
