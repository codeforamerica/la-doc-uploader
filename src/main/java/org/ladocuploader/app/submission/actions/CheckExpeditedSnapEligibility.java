package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.data.TransmissionRepositoryService;
import org.ladocuploader.app.data.enums.TransmissionType;
import org.ladocuploader.app.submission.conditions.IsApplyingForExpeditedSnap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class CheckExpeditedSnapEligibility implements Action {
  
  BigDecimal INCOME_LIMIT = new BigDecimal("150.00");
  BigDecimal MONEY_ON_HAND_LIMIT = new BigDecimal("100.00");

  @Override
  public void run(Submission submission) {
    Map<String, Object> inputData = submission.getInputData();
    if (inputData.containsKey("isApplyingForExpeditedSnap") && 
        inputData.get("isApplyingForExpeditedSnap").toString().equals("true")) {
      
      String householdIncomeString = inputData.getOrDefault("householdIncomeLast30Days", "0").toString();
      BigDecimal householdIncomeAmount = new BigDecimal(householdIncomeString).setScale(2, RoundingMode.HALF_UP);
      
      String moneyOnHandString = inputData.getOrDefault("expeditedMoneyOnHandAmount", "0").toString();
      BigDecimal moneyOnHandAmount = new BigDecimal(moneyOnHandString).setScale(2, RoundingMode.HALF_UP);
      
      String rentString = inputData.getOrDefault("householdRentAmount", "0").toString();
      BigDecimal rentAmount = new BigDecimal(rentString).setScale(2, RoundingMode.HALF_UP);
      
      boolean isMigrantOrSeasonalFarmWorker = inputData.get("migrantOrSeasonalFarmWorkerInd").toString().equals("true");
      boolean hasUtilities = inputData.containsKey("householdUtilitiesExpenses[]") && !inputData.get("householdUtilitiesExpenses[]").toString().equals("None");
      
      // Household has less than 150 in monthly income, including cash on hand is less than or equal to 100
      // BigDecimal compareTo returns -1 if less than, 0 if equal, 1 if greater than
      boolean isEligibleByCashOnHand = householdIncomeAmount.compareTo(INCOME_LIMIT) <= 0
          && moneyOnHandAmount.compareTo(MONEY_ON_HAND_LIMIT) <= 0;
          
      // Is Migrant or Seasonal farmworker household with cash on hand less than or equal to 100
      boolean isEligibleByMigrantOrSeasonalFarmWorker = isMigrantOrSeasonalFarmWorker
          && moneyOnHandAmount.compareTo(MONEY_ON_HAND_LIMIT) <= 0;
      
      // Households combined monthly income + cash on hand is less than the total monthly rent/mortgage + utilities
      BigDecimal utilitiesTotal = BigDecimal.ZERO;
      if (hasUtilities) {
        List<String> utilityTypes = (List<String>) inputData.get("householdUtilitiesExpenses[]");
        utilitiesTotal = utilityTypes.stream()
            .map(type -> inputData.getOrDefault("householdUtilitiesExpenseAmount_wildcard_" + type, "0").toString())
            .map(stringValue -> new BigDecimal(stringValue).setScale(2, RoundingMode.HALF_UP))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
      }
      
      boolean isEligibleByIncomeAndCashOnHandLessThanExpenses = householdIncomeAmount.add(moneyOnHandAmount).compareTo(rentAmount.add(utilitiesTotal)) <= 0;

      boolean isEligibleForExpeditedSnap = isEligibleByCashOnHand || isEligibleByMigrantOrSeasonalFarmWorker || isEligibleByIncomeAndCashOnHandLessThanExpenses;
      submission.getInputData().put("isEligibleForExpeditedSnap", String.valueOf(isEligibleForExpeditedSnap));
    }
  }
}