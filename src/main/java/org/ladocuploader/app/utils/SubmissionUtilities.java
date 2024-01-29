package org.ladocuploader.app.utils;

import formflow.library.data.Submission;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static formflow.library.inputs.FieldNameMarkers.DYNAMIC_FIELD_MARKER;
import static java.util.Collections.emptyList;
import static org.ladocuploader.app.utils.Parish.ORLEANS;

public class SubmissionUtilities {
    public static final String ENCRYPTED_SSNS_INPUT_NAME = "householdMemberEncryptedSSN";

    public static Long expiryHours = 2L;

    public static final DateTimeFormatter MM_DD_YYYY = DateTimeFormatter.ofPattern("M/d/uuuu");

    public static final Map<String, String> PDF_EDUCATION_MAP = new HashMap<>();
    public static final Map<String, String> PDF_MARITAL_STATUS_MAP = new HashMap<>();
    public static final Map<String, String> PDF_RELATIONSHIP_MAP = new HashMap<>();

    public static final LocalDate ECE_CUTOFF_DATE = LocalDate.parse("2019-09-30");
    public static final LocalDate WIC_CUTOFF_DATE = LocalDate.parse("2020-02-29");

    static {
        PDF_EDUCATION_MAP.put("firstGrade", "1st grade");
        PDF_EDUCATION_MAP.put("secondGrade", "2nd grade");
        PDF_EDUCATION_MAP.put("thirdGrade", "3rd grade");
        PDF_EDUCATION_MAP.put("fourthGrade", "4th grade");
        PDF_EDUCATION_MAP.put("fifthGrade", "5th grade");
        PDF_EDUCATION_MAP.put("sixthGrade", "6th grade");
        PDF_EDUCATION_MAP.put("seventhGrade", "7th grade");
        PDF_EDUCATION_MAP.put("eighthGrade", "8th grade");
        PDF_EDUCATION_MAP.put("ninthGrade", "9th grade");
        PDF_EDUCATION_MAP.put("tenthGrade", "10th grade");
        PDF_EDUCATION_MAP.put("eleventhGrade", "11th grade");
        PDF_EDUCATION_MAP.put("highSchoolOrEquivalent", "High school / GED");
        PDF_EDUCATION_MAP.put("associatesDegree", "Associate's degree");
        PDF_EDUCATION_MAP.put("bachelorsDegree", "Bachelor's degree");
        PDF_EDUCATION_MAP.put("graduateDegree", "Graduate/Master's degree");
        PDF_EDUCATION_MAP.put("certificateOrDiploma", "Other certificate or diploma");
        PDF_EDUCATION_MAP.put("noFormalEducation", "None");
        PDF_EDUCATION_MAP.put("notSure", "Not sure");

        PDF_MARITAL_STATUS_MAP.put("NeverMarried", "Never Married");
        PDF_MARITAL_STATUS_MAP.put("MarriedLivingWithSpouse", "Married");
        PDF_MARITAL_STATUS_MAP.put("MarriedNotLivingWithSpouse", "Married");
        PDF_MARITAL_STATUS_MAP.put("LegallySeparated", "Separated");
        PDF_MARITAL_STATUS_MAP.put("Divorced", "Divorced");
        PDF_MARITAL_STATUS_MAP.put("Widowed", "Widowed");

        PDF_RELATIONSHIP_MAP.put("child", "child");
        PDF_RELATIONSHIP_MAP.put("stepChild", "step child");
        PDF_RELATIONSHIP_MAP.put("spouse", "spouse");
        PDF_RELATIONSHIP_MAP.put("partner", "partner");
        PDF_RELATIONSHIP_MAP.put("sibling", "sibling");
        PDF_RELATIONSHIP_MAP.put("stepSibling", "step sibling");
        PDF_RELATIONSHIP_MAP.put("halfSibling", "half sibling");
        PDF_RELATIONSHIP_MAP.put("parent", "parent");
        PDF_RELATIONSHIP_MAP.put("grandparent", "grandparent");
        PDF_RELATIONSHIP_MAP.put("childsParent", "child's parent");
        PDF_RELATIONSHIP_MAP.put("auntOrUncle", "aunt or uncle");
        PDF_RELATIONSHIP_MAP.put("nieceOrNephew", "niece or nephew");
        PDF_RELATIONSHIP_MAP.put("roommate", "roommate");
        PDF_RELATIONSHIP_MAP.put("friend", "friend");
        PDF_RELATIONSHIP_MAP.put("grandchild", "grandchild");
        PDF_RELATIONSHIP_MAP.put("other", "other");
    }
    public static String formatMoney(String value) {
        if (value == null) {
            return "";
        }

        double numericVal;
        try {
            numericVal = Double.parseDouble(value);
        } catch (NumberFormatException _e) {
            return value;
        }

        return formatMoney(numericVal);
    }

    public static String formatMoney(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        return "$" + decimalFormat.format(value);
    }

    public static boolean isOrleansParish(Submission submission) {
        return ORLEANS.name().equals(submission.getInputData().get("parish"));
    }

    public static boolean isEligibleForExperiment(Submission submission) {
        return hasHouseholdPregnancy(submission) || hasChildBornAfterCutoff(submission, ECE_CUTOFF_DATE);
    }

    public static boolean hasHouseholdPregnancy(Submission submission) {
        var pregnancyInHousehold = submission.getInputData().getOrDefault("pregnancyInd", "false");
        return "true".equals(pregnancyInHousehold);
    }

    public static boolean hasChildBornAfterCutoff(Submission submission, LocalDate cutoffDate) {
        var household = submission.getInputData().get("household");
        if (household != null) {
            for (Map<String, Object> member : ((List<Map<String, Object>>) household)) {
                if ("child".equals(member.get("householdMemberRelationship"))) {
                    var day = member.get("householdMemberBirthDay");
                    var year = member.get("householdMemberBirthYear");
                    var month = member.get("householdMemberBirthMonth");
                    LocalDate birthdate = LocalDate.parse("%s/%s/%s".formatted(month, day, year), MM_DD_YYYY);
                    if (birthdate.isAfter(cutoffDate)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isDocUploadActive(Submission submission){
        OffsetDateTime submittedAt = submission.getSubmittedAt();
        OffsetDateTime now = OffsetDateTime.now();

        if (submittedAt != null){
            OffsetDateTime expiryTime = submittedAt.plusHours(expiryHours);

            return expiryTime.isAfter(now);
        }
        return false;
    }

    public static boolean inExperimentGroup(String groupName, Submission submission) {
        return groupName.equals(submission.getInputData().get("experimentGroup"));
    }

    public static String householdMemberFullName(Map<String, Object> householdMember) {
        return householdMember.get("householdMemberFirstName") + " " + householdMember.get("householdMemberLastName");
    }

//    public ParentGuardian getParentGuardianMap(Submission submission){
//
//
//    }

    public static List<String> getHouseholdMemberNames(Submission submission) {
        ArrayList<String> names = new ArrayList<>();

        var applicantName = submission.getInputData().get("firstName") + " " + submission.getInputData().get("lastName");
        var householdMembers = (List<Map<String, Object>>) submission.getInputData().getOrDefault("household", new ArrayList<HashMap<String, Object>>());

        names.add(applicantName);
        householdMembers.forEach(hh -> names.add(householdMemberFullName(hh)));

        return names;
    }

    public static String getHouseholdMemberFullnameByUUID(String uuid, Map<String, Object> inputData) {
        if ("you".equals(uuid)) {
            return "%s %s".formatted(inputData.get("firstName"), inputData.get("lastName"));
        }

        var members = (List<Map<String, Object>>) inputData.getOrDefault("household", emptyList());
        for (var member : members) {
            if (uuid.equals(member.get("uuid"))) {
                return householdMemberFullName(member);
            }
        }
        return "";
    }
    public static ArrayList<HashMap<String, Object>> getHouseholdIncomeReviewItems(Submission submission) {
        var applicantFullName = submission.getInputData().getOrDefault("firstName", "") + " " + submission.getInputData().getOrDefault("lastName", "");
        var notYetShownNames = getHouseholdMemberNames(submission);
        ArrayList<HashMap<String, Object>> items = new ArrayList<>();

        for (var job : (List<HashMap<String, Object>>) submission.getInputData().getOrDefault("income", new ArrayList<HashMap<String, Object>>())) {
            var item = new HashMap<String, Object>();
            var name = job.get("householdMemberJobAdd").equals("you") ? applicantFullName : job.get("householdMemberJobAdd");
            item.put("name", name);
            item.put("itemType", "job");
            item.put("jobName", job.get("employerName"));
            item.put("isApplicant", name.equals(applicantFullName));
            // TODO: handle income types - hourly vs. non hourly
            var payPeriod = job.getOrDefault("jobPaidByHour", "false").equals("true") ? "Hourly, " + job.get("hoursPerWeek").toString() + " hours per week" : job.getOrDefault("payPeriod", "It varies").toString();
            item.put("payPeriod", payPeriod);

            // TODO: add wage amount and not future income
            var payAmount = job.getOrDefault("jobPaidByHour", "false").equals("true") ? job.get("hourlyWage").toString() : job.get("payPeriodAmount").toString();
            item.put("income", formatMoney(payAmount));
            item.put("uuid", job.get("uuid"));

            notYetShownNames.remove(name);
            items.add(item);
        }

        // Add any household members that didn't have income entries
        notYetShownNames.forEach(name -> {
            var item = new HashMap<String, Object>();
            item.put("name", name);
            item.put("itemType", "no-jobs-added");
            item.put("isApplicant", name.equals(applicantFullName));

            items.add(item);
        });

        // Sort the list so the applicant shows up first and the rest of the names are alphabetical
        items.sort(Comparator.comparing(
                job -> (String)job.get("name"),
                (a, b) -> {
                    if (a.equals(applicantFullName) && !b.equals(applicantFullName)) {
                        return -1;
                    } else if (b.equals(applicantFullName) && !a.equals(applicantFullName)) {
                        return 1;
                    } else {
                        return a.compareTo(b);
                    }
                }));

        // Set combineWithPrevious on items after the first one for the same person
        for (var i = 0; i < items.size(); i++) {
            var combineWithPrevious = (i > 0) && items.get(i - 1).get("name").equals(items.get(i).get("name"));
            items.get(i).put("combineWithPrevious", combineWithPrevious);
        }

        items.add(new HashMap<String, Object>() {{
            put("name", null);
            put("itemType", "household-total");
            put("income", formatMoney(new IncomeCalculator(submission).totalFutureEarnedIncome()));
        }});

        return items;
    }

    public static String getDecryptedSSNKeyName(String uuid) {
        return "householdMemberSsn%s%s".formatted(DYNAMIC_FIELD_MARKER, uuid);
    }
}
