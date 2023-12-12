package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.data.SubmissionTestBuilder;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HouseholdDetailsPreparerTest {

    private final HouseholdDetailsPreparer preparer = new HouseholdDetailsPreparer();

    @Test
    public void testWithHouseholdMember() {
        Submission submission = new SubmissionTestBuilder()
                .withHouseholdMember("Betty", "White", "10", "2", "1999", "child", "F", "NeverMarried", "firstGrade", "123456789")
                .build();
        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);
        assertThat(result.get("householdMaritalStatus0"))
                .isEqualTo(new SingleField("householdMaritalStatusFormatted", "Never Married", 1));
        assertThat(result.get("householdHighestEducation0"))
                .isEqualTo(new SingleField("householdHighestEducationFormatted", "1st grade", 1));
        assertThat(result.get("householdBirthday0"))
                .isEqualTo(new SingleField("householdBirthdayFormatted", "2/10/1999", 1));
        assertThat(result.get("householdUSCitizen0"))
                .isEqualTo(new SingleField("householdUSCitizenDerived", "Yes", 1));
    }

    @Test
    public void testWithNonCitizenHouseholdMember() {
        Submission submission = new SubmissionTestBuilder()
                .withHouseholdMember("Betty", "White", "10", "2", "1999", "child", "F", "NeverMarried", "firstGrade", "123456789")
                .withNonCitizens(List.of("betty-white"))
                .build();
        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);
        assertThat(result.get("householdMaritalStatus0"))
                .isEqualTo(new SingleField("householdMaritalStatusFormatted", "Never Married", 1));
        assertThat(result.get("householdHighestEducation0"))
                .isEqualTo(new SingleField("householdHighestEducationFormatted", "1st grade", 1));
        assertThat(result.get("householdBirthday0"))
                .isEqualTo(new SingleField("householdBirthdayFormatted", "2/10/1999", 1));
        assertThat(result.get("householdUSCitizen0"))
                .isEqualTo(new SingleField("householdUSCitizenDerived", "No", 1));
    }
}