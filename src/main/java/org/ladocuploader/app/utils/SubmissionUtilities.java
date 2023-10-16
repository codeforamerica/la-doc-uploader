package org.ladocuploader.app.utils;

import formflow.library.data.Submission;

import java.text.DecimalFormat;
import java.util.*;

public class SubmissionUtilities {

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

    public static String householdMemberFullName(Map<String, String> householdMember) {
        return householdMember.get("householdMemberFirstName") + " " + householdMember.get("householdMemberLastName");
    }

    public static List<String> getHouseholdMemberNames(Submission submission) {
        ArrayList<String> names = new ArrayList<>();

        var applicantName = submission.getInputData().get("firstName") + " " + submission.getInputData().get("lastName");
        var householdMembers = (List<Map<String, String>>) submission.getInputData().getOrDefault("household", new ArrayList<HashMap<String, Object>>());

        names.add(applicantName);
        householdMembers.forEach(hh -> names.add(householdMemberFullName(hh)));

        return names;
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
            var item = items.get(i);
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
}
