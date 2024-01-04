package org.ladocuploader.app.submission.actions;

import formflow.library.data.Submission;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.utils.TestUtils;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FormatSubmittedAtDateTest {

  private final FormatSubmittedAtDate action = new FormatSubmittedAtDate();

  ZoneId zoneId = ZoneId.systemDefault();

  @Test
  public void testWeekday() {
    OffsetDateTime friday = TestUtils.makeOffsetDateTime("2023-12-22");
    Submission submission = Submission.builder()
        .urlParams(new HashMap<>())
        .inputData(new HashMap<>())
        .submittedAt(friday)
        .build();

    action.run(submission);

    String formattedDate = "December 22, 2023";
    String interviewDate = "January 5, 2024";
    assertThat(submission.getInputData().get("formattedSubmittedAt")).isEqualTo(formattedDate);
    assertThat(submission.getInputData().get("interviewDate")).isEqualTo(interviewDate);
  }

  @Test
  public void testSaturday() {
//    Date friday = new LocalDate("2023-12-23").toDate();
    OffsetDateTime friday = TestUtils.makeOffsetDateTime("2023-12-23");
    Submission submission = Submission.builder()
        .urlParams(new HashMap<>())
        .inputData(new HashMap<>())
        .submittedAt(friday)
        .build();

    action.run(submission);

    String formattedDate = "December 23, 2023";
    String interviewDate = "January 5, 2024";
    assertThat(submission.getInputData().get("formattedSubmittedAt")).isEqualTo(formattedDate);
    assertThat(submission.getInputData().get("interviewDate")).isEqualTo(interviewDate);
  }

  @Test
  public void testSunday() {
    OffsetDateTime friday = TestUtils.makeOffsetDateTime("2023-12-24");
//    Date friday = new LocalDate("2023-12-24").toDate();
    Submission submission = Submission.builder()
        .urlParams(new HashMap<>())
        .inputData(new HashMap<>())
        .submittedAt(friday)
        .build();

    action.run(submission);

    String formattedDate = "December 24, 2023";
    String interviewDate = "January 5, 2024";
    assertThat(submission.getInputData().get("formattedSubmittedAt")).isEqualTo(formattedDate);
    assertThat(submission.getInputData().get("interviewDate")).isEqualTo(interviewDate);
  }

}