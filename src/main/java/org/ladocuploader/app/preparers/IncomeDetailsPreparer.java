package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.ladocuploader.app.utils.IncomeCalculator;
import org.ladocuploader.app.utils.SubmissionUtilities;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IncomeDetailsPreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();
    var selfEmploymentIncome = "false";

    var income = (List<Map<String, Object>>) submission.getInputData().get("income");
    if (income != null) {
      int nonSelfEmploymentIdx = 1;
      int selfEmploymentIdx = 1;
      for (int i = 0; i < income.size(); i++) {
        Map<String, Object> incomeDetails = income.get(i);

        boolean isSelfEmployed = Boolean.parseBoolean((String) incomeDetails.getOrDefault("selfEmployed", "false"));
        var householdMemberUUID = (String) incomeDetails.getOrDefault("householdMemberJobAdd", "");
        var employeeName = SubmissionUtilities.getHouseholdMemberFullnameByUUID(householdMemberUUID, submission.getInputData());
        var employerName = incomeDetails.get("employerName");
        var hoursPerWeek = incomeDetails.get("hoursPerWeek");

        if (isSelfEmployed) {
          selfEmploymentIncome = "true";
          var monthlyIncome = IncomeCalculator.futureIncomeForJob(incomeDetails);
          results.put("selfEmploymentName" + selfEmploymentIdx, new SingleField("selfEmploymentName", employeeName, selfEmploymentIdx));
          results.put("selfEmploymentDesc" + selfEmploymentIdx, new SingleField("selfEmploymentDesc", (String) employerName, selfEmploymentIdx));
          results.put("selfEmploymentMonthlyIncome" + selfEmploymentIdx, new SingleField("selfEmploymentMonthlyIncome", "%.2f".formatted(monthlyIncome), selfEmploymentIdx));
          results.put("selfEmploymentHoursPerWeek" + selfEmploymentIdx, new SingleField("selfEmploymentHoursPerWeek", (String) hoursPerWeek, selfEmploymentIdx));
          selfEmploymentIdx++;
        } else {
          var payPeriod = incomeDetails.get("payPeriod");
          var hourlyWage = incomeDetails.get("hourlyWage");
          results.put("employeeName" + nonSelfEmploymentIdx, new SingleField("employeeName", employeeName, nonSelfEmploymentIdx));
          results.put("employer" + nonSelfEmploymentIdx, new SingleField("employer", (String) employerName, nonSelfEmploymentIdx));
          results.put("employmentPayFreq" + nonSelfEmploymentIdx, new SingleField("employmentPayFreq", (String) payPeriod, nonSelfEmploymentIdx));
          results.put("employeeHoursPerWeek" + nonSelfEmploymentIdx, new SingleField("employeeHoursPerWeek", (String) hoursPerWeek, nonSelfEmploymentIdx));
          results.put("employeeHourlyWage" + nonSelfEmploymentIdx, new SingleField("employeeHourlyWage", (String) hourlyWage, nonSelfEmploymentIdx));
          // only fill this out if job is not paid by the hour
          if (incomeDetails.get("jobPaidByHour").equals("false")) {
            var payPeriodAmount = incomeDetails.get("payPeriodAmount");
            results.put("employmentPayDescription" + nonSelfEmploymentIdx, new SingleField("employmentPayDescription", payPeriodAmount + " per pay period", nonSelfEmploymentIdx));
          }
          nonSelfEmploymentIdx++;
        }
      }
    }
    results.put("selfEmploymentIncome", new SingleField("selfEmploymentIncome", selfEmploymentIncome, null));
    return results;
  }
}
