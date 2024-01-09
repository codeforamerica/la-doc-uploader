package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;


/**
 *
 */
@Component
public class FormatSubmittedAtDate implements Action {

  private static final long ONE_DAY = 1L;
  private static final long TEN_BUSINESS_DAYS = ONE_DAY * 14;

  public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMMM d, yyyy");

  @Override
  public void run(Submission submission) {
    OffsetDateTime submittedAt = submission.getSubmittedAt();

    if (submittedAt != null) {
      String formattedSubmittedAt = submittedAt.format(DATE_FORMAT);
      submission.getInputData().put("formattedSubmittedAt", formattedSubmittedAt);
      OffsetDateTime interviewDate = submittedAt.plusDays(TEN_BUSINESS_DAYS);
      if (submittedAt.getDayOfWeek() == DayOfWeek.SATURDAY) {
        interviewDate = interviewDate.minusDays(ONE_DAY);
      } else if (submittedAt.getDayOfWeek() == DayOfWeek.SUNDAY) {
        interviewDate = interviewDate.minusDays(ONE_DAY * 2);
      }

      submission.getInputData().put("interviewDate", interviewDate.format(DATE_FORMAT));
    }
  }
}
