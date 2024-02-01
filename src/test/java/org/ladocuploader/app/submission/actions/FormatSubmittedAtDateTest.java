package org.ladocuploader.app.submission.actions;

import formflow.library.data.Submission;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.testutils.TestUtils;

import java.time.OffsetDateTime;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class FormatSubmittedAtDateTest {

  private final FormatSubmittedAtDate action = new FormatSubmittedAtDate();

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
    OffsetDateTime saturday = TestUtils.makeOffsetDateTime("2023-12-23");
    Submission submission = Submission.builder()
        .urlParams(new HashMap<>())
        .inputData(new HashMap<>())
        .submittedAt(saturday)
        .build();

    action.run(submission);

    String formattedDate = "December 23, 2023";
    String interviewDate = "January 5, 2024";
    assertThat(submission.getInputData().get("formattedSubmittedAt")).isEqualTo(formattedDate);
    assertThat(submission.getInputData().get("interviewDate")).isEqualTo(interviewDate);
  }

  @Test
  public void testSunday() {
    OffsetDateTime sunday = TestUtils.makeOffsetDateTime("2023-12-24");
    Submission submission = Submission.builder()
        .urlParams(new HashMap<>())
        .inputData(new HashMap<>())
        .submittedAt(sunday)
        .build();

    action.run(submission);

    String formattedDate = "December 24, 2023";
    String interviewDate = "January 5, 2024";
    assertThat(submission.getInputData().get("formattedSubmittedAt")).isEqualTo(formattedDate);
    assertThat(submission.getInputData().get("interviewDate")).isEqualTo(interviewDate);
  }

}