package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
@Component
public class FormatSubmittedAtDate implements Action {
  public final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM d, yyyy");
  private static final long ONE_DAY = 1000 * 60 * 60 * 24;
  private static final long TEN_BUSINESS_DAYS = ONE_DAY * 14;

  private static final int SATURDAY = 6;
  private static final int SUNDAY = 7;

  @Override
  public void run(Submission submission) {
    Date submittedAt = submission.getSubmittedAt();

    if (submittedAt != null) {
      String formattedSubmittedAt = DATE_FORMAT.format(submittedAt);
      submission.getInputData().put("formattedSubmittedAt", formattedSubmittedAt);

      DateTime submittedDate = new DateTime(submittedAt);
      long tenDaysLater = submittedAt.getTime() + TEN_BUSINESS_DAYS;
      if (submittedDate.getDayOfWeek() == SATURDAY) {
        tenDaysLater -= ONE_DAY;
      } else if (submittedDate.getDayOfWeek() == SUNDAY) {
        tenDaysLater -= 2 * ONE_DAY;
      }
      String interviewDate = DATE_FORMAT.format(new Date(tenDaysLater));
      submission.getInputData().put("interviewDate", interviewDate);
    }
  }
}
