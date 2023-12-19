package org.ladocuploader.app.data;

import formflow.library.data.Submission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmissionTestBuilder {

    private final Submission submission = new Submission();

    public Submission build() {
        return submission;
    }

    public SubmissionTestBuilder withPersonalInfo(
            String firstName, String lastName,
            String birthDay, String birthMonth, String birthYear,
            String relationship, String sex, String maritalStatus, String education, String ssn) {
        submission.getInputData().put("firstName", firstName);
        submission.getInputData().put("lastName", lastName);
        submission.getInputData().put("birthDay", birthDay);
        submission.getInputData().put("birthMonth", birthMonth);
        submission.getInputData().put("birthYear", birthYear);
        submission.getInputData().put("relationship", relationship);
        submission.getInputData().put("sex", sex);
        submission.getInputData().put("maritalStatus", maritalStatus);
        submission.getInputData().put("highestEducation", education);
        submission.getInputData().put("encryptedSSN", ssn);
        return this;
    }

    public SubmissionTestBuilder withHouseholdMember(
            String firstName, String lastName,
            String birthDay, String birthMonth, String birthYear,
            String relationship, String sex, String maritalStatus, String education, String ssn) {
        List<Map<String, Object>> household = (List<Map<String, Object>>) submission.getInputData().get("household");
        if (household == null) {
            household = new ArrayList<>();
        }

        Map<String, Object> member = new HashMap<>();
        String uuid = "%s-%s".formatted(firstName, lastName).toLowerCase();
        member.put("uuid", uuid);
        member.put("householdMemberFirstName", firstName);
        member.put("householdMemberLastName", lastName);
        member.put("householdMemberBirthDay", birthDay);
        member.put("householdMemberBirthMonth", birthMonth);
        member.put("householdMemberBirthYear", birthYear);
        member.put("householdMemberRelationship", relationship);
        member.put("householdMemberSex", sex);
        member.put("householdMemberMaritalStatus", maritalStatus);
        member.put("householdMemberHighestEducation", education);
        member.put("householdMemberEncryptedSSN", ssn);

        household.add(member);
        submission.getInputData().put("household", household);
        return this;
    }

    public SubmissionTestBuilder withNonCitizens(List<String> uuids) {
        submission.getInputData().put("nonCitizens[]", uuids);
        return this;
    }

    public SubmissionTestBuilder withStudents(List<String> uuids) {
        submission.getInputData().put("students[]", uuids);
        return this;
    }

    public SubmissionTestBuilder withHomelessness(List<String> uuids) {
        submission.getInputData().put("homeless[]", uuids);
        return this;
    }

    public SubmissionTestBuilder withPersonalSituations(List<String> uuids) {
        submission.getInputData().put("personalSituationDisability[]", uuids);
        return this;
    }

    public SubmissionTestBuilder withPregnancies(List<String> uuids) {
        submission.getInputData().put("pregnancies[]", uuids);
        return this;
    }
}
