package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.ladocuploader.app.utils.IncomeCalculator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IncomeDetailsPreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    var income = (List<Map<String, Object>>) submission.getInputData().get("income");
    if (income != null) {
      int nonSelfEmploymentIdx = 1;
      int selfEmploymentIdx = 1;
      for (int i = 0; i < income.size(); i++) {
        Map<String, Object> incomeDetails = income.get(i);

        boolean isSelfEmployed = Boolean.parseBoolean((String) incomeDetails.getOrDefault("selfEmployed", "false"));
        var employeeName = incomeDetails.get("householdMemberJobAdd");
        var employerName = incomeDetails.get("employerName");
        var hoursPerWeek = incomeDetails.get("hoursPerWeek");

        if (isSelfEmployed) {
          var monthlyIncome = IncomeCalculator.futureIncomeForJob(incomeDetails);
          results.put("selfEmploymentName" + i, new SingleField("selfEmploymentName", (String) employeeName, selfEmploymentIdx));
          results.put("selfEmploymentDesc" + i, new SingleField("selfEmploymentDesc", (String) employerName, selfEmploymentIdx));
          results.put("selfEmploymentMonthlyIncome" + i, new SingleField("selfEmploymentMonthlyIncome", Double.toString(monthlyIncome), selfEmploymentIdx));
          results.put("selfEmploymentHoursPerWeek" + i, new SingleField("selfEmploymentHoursPerWeek", (String) hoursPerWeek, selfEmploymentIdx));
          selfEmploymentIdx++;
        } else {
          var payPeriod = incomeDetails.get("payPeriod");
          var hourlyWage = incomeDetails.get("hourlyWage");
          results.put("employeeName" + i, new SingleField("employeeName", (String) employeeName, nonSelfEmploymentIdx));
          results.put("employerName" + i, new SingleField("employerName", (String) employerName, nonSelfEmploymentIdx));
          results.put("employmentPayFreq" + i, new SingleField("employmentPayFreq", (String) payPeriod, nonSelfEmploymentIdx));
          results.put("employeeHoursPerWeek" + i, new SingleField("employeeHoursPerWeek", (String) hoursPerWeek, nonSelfEmploymentIdx));
          results.put("employeeHourlyWage" + i, new SingleField("employeeHourlyWage", (String) hourlyWage, nonSelfEmploymentIdx));
          nonSelfEmploymentIdx++;
        }
      }
    }
    return results;
  }
}
