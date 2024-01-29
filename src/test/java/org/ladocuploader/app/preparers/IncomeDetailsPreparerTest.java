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
    assertThat(results).isEmpty();
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
    assertThat(results.size()).isEqualTo(18);
    assertThat(results.get("employeeName0")).isEqualTo(new SingleField("employeeName", "Joka Aksj", 1));
    assertThat(results.get("employerName0")).isEqualTo(new SingleField("employerName", "CfA", 1));
    assertThat(results.get("employmentPayFreq0")).isEqualTo(new SingleField("employmentPayFreq", "hourly", 1));
    assertThat(results.get("employeeHoursPerWeek0")).isEqualTo(new SingleField("employeeHoursPerWeek", "10", 1));
    assertThat(results.get("employeeHourlyWage0")).isEqualTo(new SingleField("employeeHourlyWage", "11", 1));

  }

  @Test
  public void testPreparerNameIsYou() {
    Submission submission = new SubmissionTestBuilder()
        .withPersonalInfo("testFirstName", "testLastName", "12", "12", "1991", "", "F", "", "", "")
        .withJob("you", "CfA", "10", "11", "false", "true").build();

    var results = preparer.prepareSubmissionFields(submission, null);
    assertThat(results.get("employeeName0").toString().equals("testFirstName testLastName"));
  }

  @Test
  public void testPreparerIsNotYou() {
    Submission submission = new SubmissionTestBuilder()
        .withPersonalInfo("testFirstName", "testLastName", "12", "12", "1991", "", "F", "", "", "")
        .withJob("first last", "CfA", "10", "11", "false", "true").build();

    var results = preparer.prepareSubmissionFields(submission, null);
    assertThat(results.get("employeeName0").toString().equals("first last"));
  }

}