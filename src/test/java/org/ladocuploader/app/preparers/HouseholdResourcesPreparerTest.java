package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HouseholdResourcesPreparerTest {

  private final HouseholdResourcesPreparer preparer = new HouseholdResourcesPreparer();

  @Test
  public void shouldntAddAnythingForNoResources() {
    Submission submission = new Submission();
    Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);
    assertThat(result).isEmpty();
  }

  @Test
  public void shouldAddFieldsForHouseholdResources() {
    Submission submission = new SubmissionTestBuilder()
        .withPersonalInfo("Person", "One", "","","","","","","","")
        .with("moneyOnHandTypes[]", List.of("Stonks"))
        .with("moneyOnHandValue_wildcard_Mutual funds", "10")
        .with("moneyOnHandValue_wildcard_Savings bond", "20")
        .with("moneyOnHandValue_wildcard_Stocks", "30")
        .with("moneyOnHandOwner_wildcard_Mutual funds", "you")
        .with("moneyOnHandOwner_wildcard_Savings bond", "Person Two")
        .with("moneyOnHandOwner_wildcard_Stocks", "Person Three")
        .build();

    Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);

    assertThat(result.size()).isEqualTo(9);
    assertThat(result.get("moneyOnHandType1"))
        .isEqualTo(new SingleField("moneyOnHandType", "Mutual funds", 1));
    assertThat(result.get("moneyOnHandAmount1"))
        .isEqualTo(new SingleField("moneyOnHandAmount", "10", 1));
    assertThat(result.get("moneyOnHandOwner1"))
        .isEqualTo(new SingleField("moneyOnHandOwner", "Person One", 1));

    assertThat(result.get("moneyOnHandType2"))
        .isEqualTo(new SingleField("moneyOnHandType", "Savings bond", 2));
    assertThat(result.get("moneyOnHandAmount2"))
        .isEqualTo(new SingleField("moneyOnHandAmount", "20", 2));
    assertThat(result.get("moneyOnHandOwner2"))
        .isEqualTo(new SingleField("moneyOnHandOwner", "Person Two", 2));

    assertThat(result.get("moneyOnHandType3"))
        .isEqualTo(new SingleField("moneyOnHandType", "Stocks", 3));
    assertThat(result.get("moneyOnHandAmount3"))
        .isEqualTo(new SingleField("moneyOnHandAmount", "30", 3));
    assertThat(result.get("moneyOnHandOwner3"))
        .isEqualTo(new SingleField("moneyOnHandOwner", "Person Three", 3));
  }

}