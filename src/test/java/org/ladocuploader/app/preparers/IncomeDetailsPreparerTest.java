package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.SingleField;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class IncomeDetailsPreparerTest {

  private final IncomeDetailsPreparer preparer = new IncomeDetailsPreparer();

  @Test
  public void testNoJobs() {
    var results = preparer.prepareSubmissionFields(new Submission(), null);
    // 1 entry indicating no jobs
    assertThat(results.size()).isEqualTo(1);
    assertThat(results.get("selfEmploymentIncome")).isEqualTo(new SingleField("selfEmploymentIncome", "false", null));
  }

  @Test
  public void testMultipleJobs() {
    Submission submission = new SubmissionTestBuilder()
        .withHouseholdMember("Joka", "Aksj", "", "", "", "", "", "", "", "", new ArrayList<>(), "")
        .withHouseholdMember("Bsod", "Aksj", "", "", "", "", "", "", "", "", new ArrayList<>(), "")
        .withHouseholdMember("Olas", "Aksj", "", "", "", "", "", "", "", "", new ArrayList<>(), "")
        .withHouseholdMember("Ydopa", "Aksj", "", "", "", "", "", "", "", "", new ArrayList<>(), "")
        .withJob("joka-aksj", "CfA", "10", "11", "false", "true")
        .withJob("bsod-aksj", "CgA", "90", "10", "true", "true")
        .withJob("olas-aksj", "ChA", "Every 2 weeks", "50", "false", "false")
        .withJob("ydopa-aksj", "CiA", "It varies", "1000", "true", "false")
        .build();

    var results = preparer.prepareSubmissionFields(submission, null);
    assertThat(results.size()).isEqualTo(20);
    assertThat(results.get("employeeName1")).isEqualTo(new SingleField("employeeName", "Joka Aksj", 1));
    assertThat(results.get("employerName1")).isEqualTo(new SingleField("employerName", "CfA", 1));
    assertThat(results.get("employmentPayFreq1")).isEqualTo(new SingleField("employmentPayFreq", null, 1));
    assertThat(results.get("employeeHoursPerWeek1")).isEqualTo(new SingleField("employeeHoursPerWeek", "10", 1));
    assertThat(results.get("employeeHourlyWage1")).isEqualTo(new SingleField("employeeHourlyWage", "11", 1));

    assertThat(results.get("employeeName2")).isEqualTo(new SingleField("employeeName", "Olas Aksj", 2));
    assertThat(results.get("employerName2")).isEqualTo(new SingleField("employerName", "ChA", 2));
    assertThat(results.get("employmentPayFreq2")).isEqualTo(new SingleField("employmentPayFreq", "Every 2 weeks", 2));
    assertThat(results.get("employeeHoursPerWeek2")).isEqualTo(new SingleField("employeeHoursPerWeek", null, 2));
    assertThat(results.get("employeeHourlyWage2")).isEqualTo(new SingleField("employeeHourlyWage", null, 2));

    assertThat(results.get("selfEmploymentName1")).isEqualTo(new SingleField("selfEmploymentName", "Bsod Aksj", 1));
    assertThat(results.get("selfEmploymentDesc1")).isEqualTo(new SingleField("selfEmploymentDesc", "CgA", 1));
    assertThat(results.get("selfEmploymentMonthlyIncome1")).isEqualTo(new SingleField("selfEmploymentMonthlyIncome", "3900.00", 1));
    assertThat(results.get("selfEmploymentHoursPerWeek1")).isEqualTo(new SingleField("selfEmploymentHoursPerWeek", "90", 1));

    assertThat(results.get("employeeName2")).isEqualTo(new SingleField("employeeName", "Olas Aksj", 2));
    assertThat(results.get("employerName2")).isEqualTo(new SingleField("employerName", "ChA", 2));
    assertThat(results.get("employmentPayFreq2")).isEqualTo(new SingleField("employmentPayFreq", "Every 2 weeks", 2));
    assertThat(results.get("employeeHoursPerWeek2")).isEqualTo(new SingleField("employeeHoursPerWeek", null, 2));
    assertThat(results.get("employeeHourlyWage2")).isEqualTo(new SingleField("employeeHourlyWage", null, 2));

    assertThat(results.get("selfEmploymentName2")).isEqualTo(new SingleField("selfEmploymentName", "Ydopa Aksj", 2));
    assertThat(results.get("selfEmploymentDesc2")).isEqualTo(new SingleField("selfEmploymentDesc", "CiA", 2));
    assertThat(results.get("selfEmploymentMonthlyIncome2")).isEqualTo(new SingleField("selfEmploymentMonthlyIncome", "1000.00", 2));
    assertThat(results.get("selfEmploymentHoursPerWeek2")).isEqualTo(new SingleField("selfEmploymentHoursPerWeek", null, 2));

    assertThat(results.get("selfEmploymentIncome")).isEqualTo(new SingleField("selfEmploymentIncome", "true", null));
  }

  @Test
  public void testPreparerNameIsYou() {
    Submission submission = new SubmissionTestBuilder()
        .withPersonalInfo("testFirstName", "testLastName", "12", "12", "1991", "", "F", "", "", "")
        .withJob("you", "CfA", "10", "11", "false", "true").build();

    var results = preparer.prepareSubmissionFields(submission, null);
    assertThat(results.get("employeeName1").toString().equals("testFirstName testLastName"));
  }

  @Test
  public void testPreparerIsNotYou() {
    Submission submission = new SubmissionTestBuilder()
        .withPersonalInfo("testFirstName", "testLastName", "12", "12", "1991", "", "F", "", "", "")
        .withJob("first last", "CfA", "10", "11", "false", "true").build();

    var results = preparer.prepareSubmissionFields(submission, null);
    assertThat(results.get("employeeName1").toString().equals("first last"));
  }

}