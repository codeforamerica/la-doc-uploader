package org.ladocuploader.app.utils;

import formflow.library.data.Submission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IncomeCalculator {
  Submission submission;
  public IncomeCalculator(Submission submission) {
    this.submission = submission;
  }

  public Double totalFutureEarnedIncome() {
    // TODO: check for annualIncome key and use that if it is entered?
//    if submission.getInputData().
    var jobs = (List<Map<String, Object>>) submission.getInputData().getOrDefault("income", new ArrayList<Map<String, Object>>());
    var total = jobs.stream()
      .map(IncomeCalculator::futureIncomeForJob)
      .reduce(0.0d, Double::sum);

    return total;
  }

  public static double futureIncomeForJob(Map<String, Object> job) throws NumberFormatException {
    if (job.getOrDefault("jobPaidByHour", "false").toString().equals("true")) {
      var hoursPerWeek = Double.parseDouble(job.get("hoursPerWeek").toString());
      var hourlyWage = Double.parseDouble(job.get("hourlyWage").toString());
      return hoursPerWeek * hourlyWage * (52.0 / 12);
    } else {
      var payPeriod = job.getOrDefault("payPeriod", "It varies");
      var payPeriodAmount = Double.parseDouble(job.get("payPeriodAmount").toString());
      if (payPeriod == "Every week"){
        return payPeriodAmount * (52.0 / 12);
      } else if (payPeriod == "Every 2 weeks"){
        return (payPeriodAmount * ((52.0 / 2) / 12));
      } else if (payPeriod == "Twice a month"){
        return payPeriodAmount * 2;
      } else if (payPeriod == "Every month"){
        return payPeriodAmount;
      }
      // based on 30D estimate
      return payPeriodAmount;
    }
  }
}
