package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
<<<<<<< HEAD
import org.joda.time.DateTime;
=======
import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
>>>>>>> 498d11b5 (Update Submission based date field calls to use OffsetDateTime)
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

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
      OffsetDateTime tenDaysLater = submittedAt.plusDays(TEN_BUSINESS_DAYS);
      if (submittedAt.getDayOfWeek() == DayOfWeek.SATURDAY){
          tenDaysLater = tenDaysLater.minusDays(ONE_DAY);
      } else if (submittedAt.getDayOfWeek() == DayOfWeek.SUNDAY){
          tenDaysLater = tenDaysLater.minusDays(ONE_DAY * 2);
      }
      String interviewDate = tenDaysLater.format(DATE_FORMAT);

      submission.getInputData().put("interviewDate", interviewDate);
    }
  }
}
