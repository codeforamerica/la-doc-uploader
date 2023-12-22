package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.SingleField;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SpecialSituationsPreparerTest {
  public static final SpecialSituationsPreparer preparer = new SpecialSituationsPreparer();

  @Test
  public void testNoMappings() {
    var results = preparer.prepareSubmissionFields(new Submission(), null);
    assertThat(results).isEmpty();
  }

  @Test
  public void testMultipleMembersNameMappings() {
    Submission submission = new SubmissionTestBuilder()
        .withPersonalInfo("Btter", "Bll", "", "", "", "", "", "", "", "")
        .withHouseholdMember("Butter", "Bull", "", "", "", "", "", "", "", "")
        .withHouseholdMember("Batter", "Ball", "", "", "", "", "", "", "", "")
        .withHouseholdMember("Bitter", "Bill", "", "", "", "", "", "", "", "")
        .withHouseholdMember("Better", "Bell", "", "", "", "", "", "", "", "")
        .with("outOfStateBenefitsRecipients[]", List.of("butter-bull", "batter-ball"))
        .with("veterans[]", List.of("bitter-bill", "batter-ball"))
        .with("preparesFood[]", List.of("butter-bull", "you"))
        .with("fostersAgedOut[]", List.of("better-bell", "you"))
        .build();

    var results = preparer.prepareSubmissionFields(submission, null);

    assertThat(results.size()).isEqualTo(4);
    assertThat(results.get("outOfStateBenefitsRecipientsNames")).isEqualTo(new SingleField("outOfStateBenefitsRecipientsNames", "Butter Bull,Batter Ball", null));
    assertThat(results.get("veteransNames")).isEqualTo(new SingleField("veteransNames", "Batter Ball,Bitter Bill", null));
    assertThat(results.get("preparesFoodNames")).isEqualTo(new SingleField("preparesFoodNames", "Butter Bull,Btter Bll", null));
    assertThat(results.get("fostersAgedOutNames")).isEqualTo(new SingleField("fostersAgedOutNames", "Better Bell,Btter Bll", null));
  }

}