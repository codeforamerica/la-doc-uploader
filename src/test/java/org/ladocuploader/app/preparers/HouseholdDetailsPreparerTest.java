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
                .withHouseholdMember("Betty", "White", "10", "2", "1999", "halfSibling", "F", "NeverMarried", "firstGrade", "123456789", null, null)
                .build();
        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);
        assertThat(result.get("householdRelationship0"))
            .isEqualTo(new SingleField("householdRelationshipFormatted", "half sibling", 1));
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
                .withHouseholdMember("Betty", "White", "10", "2", "1999", "child", "F", "NeverMarried", "firstGrade", "123456789", null, null)
                .withNonCitizens(List.of("betty-white"))
                .build();
        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);
        assertThat(result.get("householdRelationship0"))
            .isEqualTo(new SingleField("householdRelationshipFormatted", "child", 1));
        assertThat(result.get("householdMaritalStatus0"))
                .isEqualTo(new SingleField("householdMaritalStatusFormatted", "Never Married", 1));
        assertThat(result.get("householdHighestEducation0"))
                .isEqualTo(new SingleField("householdHighestEducationFormatted", "1st grade", 1));
        assertThat(result.get("householdBirthday0"))
                .isEqualTo(new SingleField("householdBirthdayFormatted", "2/10/1999", 1));
        assertThat(result.get("householdUSCitizen0"))
                .isEqualTo(new SingleField("householdUSCitizenDerived", "No", 1));
    }

    @Test
    public void testWithRaceAndEthnicity() {
        Submission submission = new SubmissionTestBuilder()
            .withHouseholdMember("One", "Person", "10", "2", "1977",
                "child", "M", "NeverMarried", "firstGrade", "123456789",
                List.of("American Indian", "Asian", "White"), "Hispanic or Latino")
            .withHouseholdMember("Two", "Person", "10", "2", "1977",
                "child", "M", "NeverMarried", "firstGrade", "123456789",
                List.of("Black or African American"), "Not Hispanic or Latino")
            // the next should still work fine w/o the ethnicity
            .withHouseholdMember("Three", "Person", "10", "2", "1977",
                "child", "M", "NeverMarried", "firstGrade", "123456789",
                List.of("Alaskan Native"), null)
            .withHouseholdMember("Four", "Person", "10", "2", "1977",
                "child", "M", "NeverMarried", "firstGrade", "123456789",
                List.of("Native Hawaiian or Other Pacific Islander", "White"), "Not Hispanic or Latino")
            // the next should only result in "AN" which covers both races
            .withHouseholdMember("Five", "Person", "10", "2", "1977",
                "child", "M", "NeverMarried", "firstGrade", "123456789",
                List.of("American Indian", "Alaskan Native"), "Hispanic or Latino")
            .withHouseholdMember("Six", "Person", "10", "2", "1977",
                "child", "M", "NeverMarried", "firstGrade", "123456789",
                List.of("American Indian", "Alaskan Native", "Native Hawaiian or Other Pacific Islander", "White", "Asian", "Black or African American"), "Hispanic or Latino")
            .build();
        Map<String, SubmissionField> result = preparer.prepareSubmissionFields(submission, null);
        assertThat(result.get("householdRaceEthnicCode0"))
            .isEqualTo(new SingleField("householdRaceEthnicCode", "AN,AS,WH/Y", 1));
        assertThat(result.get("householdRaceEthnicCode1"))
            .isEqualTo(new SingleField("householdRaceEthnicCode", "BL/N", 2));
        assertThat(result.get("householdRaceEthnicCode2"))
            .isEqualTo(new SingleField("householdRaceEthnicCode", "AN", 3));
        assertThat(result.get("householdRaceEthnicCode3"))
            .isEqualTo(new SingleField("householdRaceEthnicCode", "PI,WH/N", 4));
        assertThat(result.get("householdRaceEthnicCode4"))
            .isEqualTo(new SingleField("householdRaceEthnicCode", "AN/Y", 5));
        assertThat(result.get("householdRaceEthnicCode5"))
            .isEqualTo(new SingleField("householdRaceEthnicCode", "AN,PI,WH,AS,BL/Y", 6));
    }
}