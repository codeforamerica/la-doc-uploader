package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HouseholdExpensesPreparerTest {
  private final HouseholdExpensesPreparer preparer = new HouseholdExpensesPreparer();


  @Test
  public void shouldntAddAnythingForNoExpenses() {
    Submission submission = new Submission();
    Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);
    assertThat(result).isEmpty();
  }

  @Test
  public void shouldAddFieldsForHouseholdExpenses() {
    Submission submission = new SubmissionTestBuilder()
        .with("householdHomeExpenses[]", List.of("mortgage", "homeOwnerInsurance"))
        .with("householdUtilitiesExpenses[]", List.of("phone", "heating"))
        .with("householdUtilitiesExpenseAmount_wildcard_phone", "10")
        .with("householdHomeExpenseAmount_wildcard_mortgage", "20")
        .with("householdHomeExpenseAmount_wildcard_homeownerInsurance", "30")
        .build();

    Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);

    assertThat(result.size()).isEqualTo(9);
    assertThat(result.get("householdExpensesType1"))
        .isEqualTo(new SingleField("householdExpensesType", "Homeowner's Insurance", 1));
    assertThat(result.get("householdExpensesAmount1"))
        .isEqualTo(new SingleField("householdExpensesAmount", "30", 1));
    assertThat(result.get("householdExpensesFreq1"))
        .isEqualTo(new SingleField("householdExpensesFreq", "Monthly", 1));

    assertThat(result.get("householdExpensesType2"))
        .isEqualTo(new SingleField("householdExpensesType", "Mortgage", 2));
    assertThat(result.get("householdExpensesAmount2"))
        .isEqualTo(new SingleField("householdExpensesAmount", "20", 2));
    assertThat(result.get("householdExpensesFreq2"))
        .isEqualTo(new SingleField("householdExpensesFreq", "Monthly", 2));

    assertThat(result.get("householdExpensesType3"))
        .isEqualTo(new SingleField("householdExpensesType", "Phone/Cell Phone", 3));
    assertThat(result.get("householdExpensesAmount3"))
        .isEqualTo(new SingleField("householdExpensesAmount", "10", 3));
    assertThat(result.get("householdExpensesFreq3"))
        .isEqualTo(new SingleField("householdExpensesFreq", "Monthly", 3));
  }

  @Test
  public void shouldCombineOtherExpenses() {
    Submission submission = new SubmissionTestBuilder()
        .with("householdHomeExpenses[]", List.of("Other"))
        .with("householdUtilitiesExpenseAmount_wildcard_otherUtilitiesExpenses", "10")
        .with("householdHomeExpenseAmount_wildcard_otherHomeExpenses", "22")
        .build();

    Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);

    assertThat(result.size()).isEqualTo(4);
    assertThat(result.get("Other"))
        .isEqualTo(new SingleField("Other", "Yes", null));
    assertThat(result.get("householdExpensesType_1"))
        .isEqualTo(new SingleField("householdExpensesType_1", "Other", null));
    assertThat(result.get("householdExpensesAmount_1"))
        .isEqualTo(new SingleField("householdExpensesAmount_1", "32.0", null));
    assertThat(result.get("householdExpensesFreq_1"))
        .isEqualTo(new SingleField("householdExpensesFreq_1", "Monthly", null));

  }
}