package org.ladocuploader.app.preparers;

import static org.assertj.core.api.Assertions.assertThat;

import formflow.library.data.Submission;
import formflow.library.pdf.SingleField;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;

class HouseholdPrepareFoodTogetherPreparerTest {
  public static final HouseholdPrepareFoodTogetherPreparer preparer = new HouseholdPrepareFoodTogetherPreparer();

  @Test
  public void shouldMapHouseholdMembersWhoDoNotPrepareFoodTogether() {
    Submission submission = new SubmissionTestBuilder()
        .withPersonalInfo("Btter", "Bll", "", "", "", "", "", "", "", "")
        .withHouseholdMember("Butter", "Bull", "", "", "", "", "", "", "", "", null, null)
        .withHouseholdMember("Batter", "Ball", "", "", "", "", "", "", "", "", null, null)
        .withHouseholdMember("Bitter", "Bill", "", "", "", "", "", "", "", "", null, null)
        .withHouseholdMember("Better", "Bell", "", "", "", "", "", "", "", "", null, null)
        .with("preparesFood[]", List.of("butter-bull", "you"))
        .build();

    var results = preparer.prepareSubmissionFields(submission, null);

    assertThat(results.size()).isEqualTo(1);
    assertThat(results.get("preparesFoodNames")).isEqualTo(new SingleField("preparesFoodNames", "Batter Ball, Bitter Bill, Better Bell", null));
  }
  
  @Test
  public void shouldMapAllHouseholdMembersIfPrepareFoodTogetherQuestionWasSkipped() {
    Submission submission = new SubmissionTestBuilder()
        .withPersonalInfo("Btter", "Bll", "", "", "", "", "", "", "", "")
        .withHouseholdMember("Butter", "Bull", "", "", "", "", "", "", "", "", null, null)
        .withHouseholdMember("Batter", "Ball", "", "", "", "", "", "", "", "", null, null)
        .withHouseholdMember("Bitter", "Bill", "", "", "", "", "", "", "", "", null, null)
        .withHouseholdMember("Better", "Bell", "", "", "", "", "", "", "", "", null, null)
        .build();

    var results = preparer.prepareSubmissionFields(submission, null);

    assertThat(results.size()).isEqualTo(1);
    assertThat(results.get("preparesFoodNames")).isEqualTo(new SingleField("preparesFoodNames", "Btter Bll, Butter Bull, Batter Ball, Bitter Bill, Better Bell", null));
  }

}