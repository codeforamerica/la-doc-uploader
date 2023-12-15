package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MaritalStatusPreparerTest {

    private final MaritalStatusPreparer preparer = new MaritalStatusPreparer();

    @Test
    public void testMaritalStatusMarriedLivingWithSpouse() {
        Submission submission = new SubmissionTestBuilder()
                .withPersonalInfo("", "", "", "", "",
                        "", "", "MarriedLivingWithSpouse", "", "")
                .build();
        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);

        assertThat(result.get("maritalStatus")).isEqualTo(new SingleField("maritalStatus", "Married", null));
    }

    @Test
    public void testMaritalStatusMarriedNotLivingWithSpouse() {
        Submission submission = new SubmissionTestBuilder()
                .withPersonalInfo("", "", "", "", "",
                        "", "", "MarriedNotLivingWithSpouse", "", "")
                .build();
        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);

        assertThat(result.get("maritalStatus")).isEqualTo(new SingleField("maritalStatus", "Married", null));
    }
}