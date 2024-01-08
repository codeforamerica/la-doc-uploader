package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.SingleField;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PregnancyDueDatePreparerTest {
  public static final PregnancyDueDatePreparer preparer = new PregnancyDueDatePreparer();

  @Test
  public void testNoPregnancyDetailsMappings() {
    var results = preparer.prepareSubmissionFields(new Submission(), null);
    assertThat(results).isEmpty();
  }

  @Test
  public void testMultipleHouseholdPregnancies() {
    Submission submission = new SubmissionTestBuilder()
        .withHouseholdMember("Butter", "Ball", "", "", "", "", "", "", "", "", null, null)
        .withHouseholdMember("Batter", "Ball", "", "", "", "", "", "", "", "", null, null)
        .withPregnancies(List.of("butter-ball", "batter-ball"), List.of("12/12/1212", "2/2/2222"))
        .build();

    var results = preparer.prepareSubmissionFields(submission, null);

    assertThat(results.size()).isEqualTo(2);
    assertThat(results.get("pregnantMemberNames")).isEqualTo(new SingleField("pregnantMemberNames", "Butter Ball,Batter Ball", null));
    assertThat(results.get("pregnantMemberDueDates")).isEqualTo(new SingleField("pregnantMemberDueDates", "12/12/1212,2/2/2222", null));
  }

}