package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PersonalSituationsPreparerTest {

    private final PersonalSituationsPreparer preparer = new PersonalSituationsPreparer();

    @Test
    public void testFalseDisablityIndicator() {
        Submission submission = new Submission();
        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);

        assertThat(result.get("disablityInd")).isEqualTo(new SingleField("personalSituationDisablityInd", "false", null));
    }

    @Test
    public void testApplicantDisablityIndicator() {
        Submission submission = new SubmissionTestBuilder()
                .withPersonalSituations(List.of("you"))
                .build();
        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);

        assertThat(result.get("disablityInd")).isEqualTo(new SingleField("personalSituationDisablityInd", "true", null));
    }

    @Test
    public void testHouseholdDisablityIndicator() {
        Submission submission = new SubmissionTestBuilder()
                .withHouseholdMember("test", "test", "","","","","","","","")
                .withPersonalSituations(List.of("test-test"))
                .build();
        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);

        assertThat(result.get("disablityInd")).isEqualTo(new SingleField("personalSituationDisablityInd", "true", null));
    }
}