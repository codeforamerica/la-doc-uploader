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
public class MoneyOnHandPreparer implements SubmissionFieldPreparer {
  private static final Map<String, String> EXPENSES = new HashMap<>();

  static {
    EXPENSES.put("householdMedicalExpenseAmount_wildcard_Dental bills", "Dental bills");
    EXPENSES.put("householdMedicalExpenseAmount_wildcard_Hospital bills", "Hospital bills");
    EXPENSES.put("householdMedicalExpenseAmount_wildcard_Prescribed medicine", "Prescribed medicine");
    EXPENSES.put("householdMedicalExpenseAmount_wildcard_Prescription drug plan premium", "Prescription drug plan premium");
    EXPENSES.put("householdMedicalExpenseAmount_wildcard_Medical appliances", "Medical appliances");
    EXPENSES.put("householdMedicalExpenseAmount_wildcard_Health insurance or Medicare premiums", "Health insurance or Medicare premiums");
    EXPENSES.put("householdMedicalExpenseAmount_wildcard_Nursing home", "Nursing home");
    EXPENSES.put("householdMedicalExpenseAmount_wildcard_Other medical expenses", "Other medical expenses");
  }

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    var expenses = (List) submission.getInputData().getOrDefault("householdMedicalExpenses[]", emptyList());
    if (!expenses.isEmpty()) {
      List<String> sortedExpenses = EXPENSES.keySet().stream().sorted().toList();
      int i = 1;
      for (String expense : sortedExpenses) {
        var expenseInput = submission.getInputData().get(expense);
        if (expenseInput != null) {
          results.put("medicalExpensesType" + i, new SingleField("medicalExpensesType", EXPENSES.get(expense), i));
          results.put("medicalExpensesAmount" + i, new SingleField("medicalExpensesAmount", (String) expenseInput, i));
          results.put("medicalExpensesFreq" + i, new SingleField("medicalExpensesFreq", "Monthly", i));
          i++;
        }
      }
    }

    return results;
  }
}
