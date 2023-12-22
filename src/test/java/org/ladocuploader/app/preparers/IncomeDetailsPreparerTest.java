package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;

import static org.assertj.core.api.Assertions.assertThat;

class IncomeDetailsPreparerTest {

  private final IncomeDetailsPreparer preparer = new IncomeDetailsPreparer();

  @Test
  public void testNoJobs() {
    var results = preparer.prepareSubmissionFields(new Submission(), null);
    assertThat(results).isEmpty();
  }

  @Test
  public void testMultipleJobs() {
    Submission submission = new SubmissionTestBuilder()
        .withJob("Joka Aksj", "CfA", "10", "11", "false", "true")
        .withJob("Bsod Aksj", "CgA", "90", "10", "true", "true")
        .withJob("Olas Aksj", "ChA", "Every 2 weeks", "50", "false", "false")
        .withJob("Ydopa Aksj", "CiA", "It varies", "1000", "true", "false")
        .build();

    var results = preparer.prepareSubmissionFields(submission, null);
    assertThat(results.size()).isEqualTo(18);
  }


}