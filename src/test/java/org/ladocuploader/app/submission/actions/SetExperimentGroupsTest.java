package org.ladocuploader.app.submission.actions;


import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ladocuploader.app.submission.actions.SetExperimentGroups.ExperimentGroup.*;

class SetExperimentGroupsTest {

  @Test
  void shouldSetExperimentGroupToControlLinkOrApply() {
    Submission submission = new Submission();
    Action setExperimentGroups = new SetExperimentGroups();
    setExperimentGroups.run(submission);
    assertThat(submission.getInputData().get("experimentGroup")).isNotNull();
    assertThat(submission.getInputData().get("experimentGroup")).isIn(APPLY, CONTROL, LINK);
  }
}