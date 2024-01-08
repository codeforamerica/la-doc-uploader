package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@Component
@Slf4j
public class MedicalExpensesPreparer implements SubmissionFieldPreparer {
  private static final String AMOUNT_PREFIX = "householdMedicalExpenseAmount_wildcard_";
  private static final Map<String, String> EXPENSES = new HashMap<>();

  static {
    EXPENSES.put("dentalBills", "Dental bills");
    EXPENSES.put("hospitalBills", "Hospital bills");
    EXPENSES.put("prescriptionMedicine", "Prescribed medicine");
    EXPENSES.put("prescriptionPremium", "Prescription drug plan premium");
    EXPENSES.put("medicalAppliances", "Medical appliances");
    EXPENSES.put("insurancePremiums", "Health insurance or Medicare premiums");
    EXPENSES.put("nursingHome", "Nursing home");
    EXPENSES.put("otherMedicalExpenses", "Other medical expenses");
  }

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    var expenses = (List) submission.getInputData().getOrDefault("householdMedicalExpenses[]", emptyList());
    if (!expenses.isEmpty()) {
      List<String> expensesList = EXPENSES.keySet().stream().toList();
      int i = 1;
      for (String expense : expensesList) {
        var expenseInput = submission.getInputData().get(AMOUNT_PREFIX + expense);
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
