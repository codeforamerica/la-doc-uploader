package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@Component
@Slf4j
public class HouseholdExpensesPreparer implements SubmissionFieldPreparer {
  private static final Map<String, String> EXPENSES = new HashMap<>();

  static {
    EXPENSES.put("householdHomeExpenseAmount_wildcard_Rent", "Rent");
    EXPENSES.put("householdHomeExpenseAmount_wildcard_Mortgage", "Mortgage");
    EXPENSES.put("householdHomeExpenseAmount_wildcard_Homeowner's Insurance", "Homeowner's Insurance");
    EXPENSES.put("householdHomeExpenseAmount_wildcard_Property Tax", "Property Tax");
    EXPENSES.put("householdHomeExpenseAmount_wildcard_Condominium Fees", "Condo Fees");
    EXPENSES.put("householdHomeExpenseAmount_wildcard_Lot Rental", "Lot Rental");
    EXPENSES.put("householdHomeExpenseAmount_wildcard_Flood Insurance", "Flood Insurance");
    EXPENSES.put("householdUtilitiesExpenseAmount_wildcard_Electricity", "Electricity");
    EXPENSES.put("householdUtilitiesExpenseAmount_wildcard_Water", "Water");
    EXPENSES.put("householdUtilitiesExpenseAmount_wildcard_Phone/Cell Phone", "Phone/Cell Phone");
    EXPENSES.put("householdUtilitiesExpenseAmount_wildcard_Garbage", "Garbage");
    EXPENSES.put("householdUtilitiesExpenseAmount_wildcard_Sewer", "Sewer");
    EXPENSES.put("householdUtilitiesExpenseAmount_wildcard_Cooking Fuel", "Gas");
  }

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    var householdUtilities = (List) submission.getInputData().getOrDefault("householdUtilitiesExpenses[]", emptyList());
    var householdExpenses = (List) submission.getInputData().getOrDefault("householdHomeExpenses[]", emptyList());
    if (!householdExpenses.isEmpty() || !householdUtilities.isEmpty()) {
      List<String> sortedExpenses = EXPENSES.keySet().stream().sorted().toList();
      int i = 1;
      for (String expense : sortedExpenses) {
        var expenseInput = submission.getInputData().get(expense);
        if (expenseInput != null) {
          results.put("householdExpensesType" + i, new SingleField("householdExpensesType", EXPENSES.get(expense), i));
          results.put("householdExpensesAmount" + i, new SingleField("householdExpensesAmount", (String) expenseInput, i));
          results.put("householdExpensesFreq" + i, new SingleField("householdExpensesFreq", "Monthly", i));
          i++;
        }
      }

      // Heating or A/C
      results.put("heatingOrCoolingInd", new SingleField("heatingOrCoolingInd", Boolean.toString(householdUtilities.contains("Heating") || householdUtilities.contains("Cooling")), null));

      // Check for "Other" - theres only one spot for this in the PDF so just combine them
      var totalOtherAmount = 0.0;
      try {
        var expensesOther = submission.getInputData().get("expensesOther");
        if (expensesOther != null) {
          totalOtherAmount += Double.parseDouble((String) expensesOther);
        }
        var expensesUtilitiesOther = submission.getInputData().get("expensesUtilitiesOther");
        if (expensesUtilitiesOther != null) {
          totalOtherAmount += Double.parseDouble((String) expensesUtilitiesOther);
        }
      } catch (NumberFormatException e) {
        log.warn("Could not parse amount", e);
      }

      if (totalOtherAmount > 0) {
        results.put("Other", new SingleField("Other", "Yes", null));

        results.put("householdExpensesType_" + i, new SingleField("householdExpensesType_" + i, "Other", null));
        results.put("householdExpensesAmount_" + i, new SingleField("householdExpensesAmount_" + i, Double.toString(totalOtherAmount), null));
        results.put("householdExpensesFreq_" + i, new SingleField("householdExpensesFreq_" + i, "Monthly", null));
      }
    }

    return results;
  }
}
