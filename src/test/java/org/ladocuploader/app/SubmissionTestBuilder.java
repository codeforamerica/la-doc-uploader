package org.ladocuploader.app;

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

    public SubmissionTestBuilder withPersonalInfo() {
        submission.getInputData().put("firstName", "Betty");
        submission.getInputData().put("lastName", "White");
        submission.getInputData().put("ssn", "111223333");
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
        member.put("householdMemberFirstName", firstName);
        member.put("householdMemberLastName", lastName);
        member.put("householdMemberBirthDay", birthDay);
        member.put("householdMemberBirthMonth", birthMonth);
        member.put("householdMemberBirthYear", birthYear);
        member.put("householdMemberRelationship", relationship);
        member.put("householdMemberSex", sex);
        member.put("householdMemberMaritalStatus", maritalStatus);
        member.put("householdMemberHighestEducation", education);
        member.put("householdMemberSsn", ssn);

        household.add(member);
        submission.getInputData().put("household", household);
        return this;
    }
}
