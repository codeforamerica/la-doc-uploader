package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MedicalExpensesPreparerTest {

  private final MedicalExpensesPreparer preparer = new MedicalExpensesPreparer();

  @Test
  public void shouldntAddAnythingForNoExpenses() {
    Submission submission = new Submission();
    Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);
    assertThat(result).isEmpty();
  }

  @Test
  public void shouldAddFieldsForMedicalExpenses() {
    Submission submission = new SubmissionTestBuilder()
        .with("householdMedicalExpenses[]", List.of("Dental bills"))
        .with("householdMedicalExpenseAmount_wildcard_Dental bills", "10")
        .with("householdMedicalExpenseAmount_wildcard_Hospital bills", "20")
        .with("householdMedicalExpenseAmount_wildcard_Nursing home", "30")
        .with("householdMedicalExpenseAmount_wildcard_Prescribed medicine", "40")
        .build();

    Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);

    assertThat(result.size()).isEqualTo(12);
    assertThat(result.get("medicalExpensesType1"))
        .isEqualTo(new SingleField("medicalExpensesType", "Dental bills", 1));
    assertThat(result.get("medicalExpensesAmount1"))
        .isEqualTo(new SingleField("medicalExpensesAmount", "10", 1));
    assertThat(result.get("medicalExpensesFreq1"))
        .isEqualTo(new SingleField("medicalExpensesFreq", "Monthly", 1));

    assertThat(result.get("medicalExpensesType2"))
        .isEqualTo(new SingleField("medicalExpensesType", "Hospital bills", 2));
    assertThat(result.get("medicalExpensesAmount2"))
        .isEqualTo(new SingleField("medicalExpensesAmount", "20", 2));
    assertThat(result.get("medicalExpensesFreq2"))
        .isEqualTo(new SingleField("medicalExpensesFreq", "Monthly", 2));

    assertThat(result.get("medicalExpensesType3"))
        .isEqualTo(new SingleField("medicalExpensesType", "Prescribed medicine", 3));
    assertThat(result.get("medicalExpensesAmount3"))
        .isEqualTo(new SingleField("medicalExpensesAmount", "40", 3));
    assertThat(result.get("medicalExpensesFreq3"))
        .isEqualTo(new SingleField("medicalExpensesFreq", "Monthly", 3));

    assertThat(result.get("medicalExpensesType4"))
        .isEqualTo(new SingleField("medicalExpensesType", "Nursing home", 4));
    assertThat(result.get("medicalExpensesAmount4"))
        .isEqualTo(new SingleField("medicalExpensesAmount", "30", 4));
    assertThat(result.get("medicalExpensesFreq4"))
        .isEqualTo(new SingleField("medicalExpensesFreq", "Monthly", 4));
  }

}