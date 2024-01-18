package org.ladocuploader.app.preparers;

import static java.util.Collections.emptyList;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.kms.endpoints.internal.Value.Int;

@Component
public class ExpeditedSnapPreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    boolean isEligibleForExpeditedSnap = (Boolean) submission.getInputData().get("isEligibleForExpeditedSnap");
    if (isEligibleForExpeditedSnap) {
      String householdIncomeLast30Days=submission.getInputData().getOrDefault("householdIncomeLast30Days", "0").toString();
      results.put("expeditedSnapIncome",  new SingleField("expeditedSnapIncome", householdIncomeLast30Days, null));

      String moneyOnHandString = submission.getInputData().getOrDefault("expeditedMoneyOnHandAmount", "0").toString();
      results.put("expeditedSnapMoneyOnHand",  new SingleField("expeditedSnapMoneyOnHand", moneyOnHandString, null));

      String householdRentAmount = submission.getInputData().getOrDefault("householdRentAmount", "0").toString();
      results.put("expeditedSnapHousingCostBool",  new SingleField("expeditedSnapHousingCostBool", householdRentAmount, null));

      var utilities = processUtilitiesExpenses(submission);
      results.put("expeditedSnapHouseholdExpensesBool",  new SingleField("expeditedSnapHouseholdExpensesBool", utilities.get("expenses").toString(), null));
      results.put("expeditedSnapHeatingBool",  new SingleField("expeditedSnapHeatingBool", utilities.get("acOrCooling").toString(), null));

      String householdPhoneExpenses = submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_phone", 0).toString();
      Boolean hasPhoneExpenses = householdPhoneExpenses.equals("0");
      results.put("expeditedSnapPhoneExpenses",  new SingleField("expeditedSnapPhoneExpenses", hasPhoneExpenses.toString(), null));

      String isMigrantOrSeasonalFarmWorker = submission.getInputData().get("migrantOrSeasonalFarmWorkerInd").toString();
      results.put("expeditedMigrantOrSeasonalWorkerBool",  new SingleField("expeditedMigrantOrSeasonalWorkerBool", isMigrantOrSeasonalFarmWorker, null));
    }
    return results;
  }

  private Map<String, Boolean> processUtilitiesExpenses(Submission submission) {
    Map<String, Boolean> results = new HashMap<>();
    boolean expenses;
    boolean acOrCooling;

    Integer electricity = (Integer) submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_electricity", 0);
    Integer water = (Integer) submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_water", 0);
    Integer garbage = (Integer) submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_garbage", 0);
    Integer sewer = (Integer) submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_sewer", 0);
    Integer cookingFuel = (Integer) submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_cookingFuel", 0);
    Integer otherUtilitiesExpenses = (Integer) submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_otherUtilitiesExpenses", 0);

    expenses = (electricity + water + garbage + sewer + cookingFuel + otherUtilitiesExpenses > 0);

    var heating = (Integer) submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_heating", 0);
    var cooling = (Integer) submission.getInputData().getOrDefault("householdUtilitiesExpenseAmount_wildcard_cooling", 0);

    acOrCooling = (heating + cooling > 0);

    results.put("expenses", expenses);
    results.put("acOrCooling", acOrCooling);

    return results;
  }
}


