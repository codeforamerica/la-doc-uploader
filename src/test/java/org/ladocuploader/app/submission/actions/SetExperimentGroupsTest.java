package org.ladocuploader.app.submission.actions;


import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ladocuploader.app.submission.actions.SetExperimentGroups.ExperimentGroup.*;

class SetExperimentGroupsTest {

  @Test
  void shouldNotSetExperimentGroupForMissingEligibilityData() {
    Submission submission = new Submission();
    Action setExperimentGroups = new SetExperimentGroups();
    setExperimentGroups.run(submission);
    assertThat(submission.getInputData().get("experimentGroup")).isNull();
  }

  @Test
  void shouldSetExperimentGroupForApplicantWithChildUnder5() {
    Submission submission = new SubmissionTestBuilder()
        .withHouseholdMember("Butter", "Ball", "10", "03", "2022", "child", "", "", "", "")
        .build();
    Action setExperimentGroups = new SetExperimentGroups();
    setExperimentGroups.run(submission);
    assertThat(submission.getInputData().get("experimentGroup")).isNotNull();
    assertThat(submission.getInputData().get("experimentGroup")).isIn(APPLY, CONTROL, LINK);
  }

  @Test
  void shouldNotSetExperimentGroupForApplicantWithChildOver5() {
    Submission submission = new SubmissionTestBuilder()
        .withHouseholdMember("Butter", "Ball", "10", "03", "2000", "child", "", "", "", "")
        .build();
    Action setExperimentGroups = new SetExperimentGroups();
    setExperimentGroups.run(submission);
    assertThat(submission.getInputData().get("experimentGroup")).isNull();
  }

  @Test
  void shouldSetExperimentGroupForHouseholdWithPregnancy() {
    Submission submission = new SubmissionTestBuilder()
        .withHouseholdMember("Butter", "Ball", "", "", "", "", "", "", "", "")
        .withPregnancies(List.of("Butter-Ball"))
        .build();
    Action setExperimentGroups = new SetExperimentGroups();
    setExperimentGroups.run(submission);
    assertThat(submission.getInputData().get("experimentGroup")).isNotNull();
    assertThat(submission.getInputData().get("experimentGroup")).isIn(APPLY, CONTROL, LINK);
  }

}