package org.ladocuploader.app.preparers;

import static java.util.Collections.emptyList;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ExpeditedSnapPreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    String isEligibleForExpeditedSnap = submission.getInputData().get("isEligibleForExpeditedSnap").toString();
    if (isEligibleForExpeditedSnap.equals("true")) {
      String householdIncomeLast30Days=submission.getInputData().getOrDefault("householdIncomeLast30Days", "0").toString();
      results.put("expeditedSnapIncome",  new SingleField("expeditedSnapIncome", householdIncomeLast30Days, null));

      String moneyOnHandString = submission.getInputData().getOrDefault("expeditedMoneyOnHandAmount", "0").toString();
      results.put("expeditedSnapMoneyOnHand",  new SingleField("expeditedSnapMoneyOnHand", moneyOnHandString, null));

      String householdRentAmount = submission.getInputData().getOrDefault("householdRentAmount", "0").toString();
      results.put("expeditedSnapHousingCost",  new SingleField("expeditedSnapHousingCost", householdRentAmount, null));

      var utilities = processUtilitiesExpenses(submission);
      boolean hasHouseholdExpenses = utilities.get("expenses");
      boolean hasHeatingExpenses = utilities.get("acOrCooling");

      results.put("expeditedSnapHouseholdExpensesBool",  new SingleField("expeditedSnapHouseholdExpensesBool", hasHouseholdExpenses ? "Yes" : "No", null));
      results.put("expeditedSnapHeatingBool",  new SingleField("expeditedSnapHeatingBool",  hasHeatingExpenses ? "Yes" : "No", null));

      String householdPhoneExpenses = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_phone", "0").toString();
      Boolean hasPhoneExpenses = !householdPhoneExpenses.equals("0");
      results.put("expeditedSnapPhoneExpensesBool",  new SingleField("expeditedSnapPhoneExpensesBool", hasPhoneExpenses ? "Yes" : "No", null));

      String isMigrantOrSeasonalFarmWorker = submission.getInputData().get("migrantOrSeasonalFarmWorkerInd").toString();
      results.put("expeditedMigrantOrSeasonalWorkerBool",  new SingleField("expeditedMigrantOrSeasonalWorkerBool", isMigrantOrSeasonalFarmWorker.equals("true") ? "Yes" : "No", null));
    }

    return results;
  }

  private Map<String, Boolean> processUtilitiesExpenses(Submission submission) {
    Map<String, Boolean> results = new HashMap<>();
    boolean expenses;
    boolean acOrCooling;

    String electricity = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_electricity", "0").toString();
    String water = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_water", "0").toString();
    String garbage = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_garbage", "0").toString();
    String sewer = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_sewer", "0").toString();
    String cookingFuel = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_cookingFuel", "0").toString();
    String otherUtilitiesExpenses = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_otherUtilitiesExpenses", "0").toString();

    expenses = (Integer.parseInt(electricity) + Integer.parseInt(water) + Integer.parseInt(garbage) + Integer.parseInt(sewer) + Integer.parseInt(cookingFuel) + Integer.parseInt(otherUtilitiesExpenses) > 0);

    var heating = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_heating", "0").toString();
    var cooling = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_cooling", "0").toString();

    acOrCooling = (Integer.parseInt(heating) + Integer.parseInt(cooling) > 0);

    results.put("expenses", expenses);
    results.put("acOrCooling", acOrCooling);

    return results;
  }
}

