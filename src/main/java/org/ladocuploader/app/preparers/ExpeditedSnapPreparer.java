package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@Component
public class ExpeditedSnapPreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();
    var householdUtilities = (List) submission.getInputData().getOrDefault("householdUtilitiesExpenses[]", emptyList());

    var isEligibleForExpeditedSnap = submission.getInputData().getOrDefault("isEligibleForExpeditedSnap", "false");
    if (isEligibleForExpeditedSnap.equals("true")) {
      String householdIncomeLast30Days = submission.getInputData().getOrDefault("householdIncomeLast30Days", "0").toString();
      results.put("expeditedSnapIncome", new SingleField("expeditedSnapIncome", householdIncomeLast30Days, null));

      String moneyOnHandString = submission.getInputData().getOrDefault("expeditedMoneyOnHandAmount", "0").toString();
      results.put("expeditedSnapMoneyOnHand", new SingleField("expeditedSnapMoneyOnHand", moneyOnHandString, null));

      String householdRentAmount = submission.getInputData().getOrDefault("householdRentAmount", "0").toString();
      results.put("expeditedSnapHousingCost", new SingleField("expeditedSnapHousingCost", householdRentAmount, null));

      results.put("expeditedSnapHouseholdExpensesBool",
          new SingleField("expeditedSnapHouseholdExpensesBool", hasHouseholdExpenses(submission) ? "Yes" : "No", null));

      String isMigrantOrSeasonalFarmWorker = submission.getInputData().get("migrantOrSeasonalFarmWorkerInd").toString();
      // Heating or A/C
      results.put("expeditedSnapHeatingBool", new SingleField("expeditedSnapHeatingBool", Boolean.toString(householdUtilities.contains("heating") || householdUtilities.contains("cooling")), null));

      // Electricity, Water, Garbage, Sewer, and/or Cooking Fuels
      results.put(
              "expeditedSnapUtilityExpensesBool",
              new SingleField(
                      "expeditedSnapUtilityExpensesBool",
                      Boolean.toString(
                              householdUtilities.contains("electricity") ||
                                      householdUtilities.contains("water") ||
                                      householdUtilities.contains("garbage") ||
                                      householdUtilities.contains("sewer") ||
                                      householdUtilities.contains("gas")
                      ),
                      null)
      );
      // Phone/ cell phone
      results.put("expeditedSnapPhoneExpensesBool", new SingleField("expeditedSnapPhoneExpensesBool", Boolean.toString(householdUtilities.contains("phone")), null));
      results.put("expeditedMigrantOrSeasonalWorkerBool",
          new SingleField("expeditedMigrantOrSeasonalWorkerBool", Boolean.toString(isMigrantOrSeasonalFarmWorker.equals("true")),
              null));
    }

    return results;
  }

  private int parseInt(String intAsString) {
    try {
      return Integer.parseInt(intAsString);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  private boolean hasHouseholdExpenses(Submission submission) {
    String electricity = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_electricity", "0")
        .toString();
    String water = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_water", "0").toString();
    String garbage = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_garbage", "0").toString();
    String sewer = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_sewer", "0").toString();
    String cookingFuel = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_cookingFuel", "0")
        .toString();
    String otherUtilitiesExpenses = submission.getInputData()
        .getOrDefault("householdUtilitiesExpenseAmount_wildcard_otherUtilitiesExpenses", "0").toString();

    return (parseInt(electricity) + parseInt(water) + parseInt(garbage) + parseInt(sewer) + parseInt(cookingFuel) + parseInt(
        otherUtilitiesExpenses) > 0);
  }

  private boolean hasHeatingExpenses(Submission submission) {
    String heating = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_heating", "0").toString();
    String cooling = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_cooling", "0").toString();

    return (parseInt(heating) + parseInt(cooling) > 0);
  }
}

