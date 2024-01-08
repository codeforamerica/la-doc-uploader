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
  private static final List<String> EXPENSES = new ArrayList<>();

  static {
    EXPENSES.add("dentalBills");
    EXPENSES.add("hospitalBills");
    EXPENSES.add("prescriptionMedicine");
    EXPENSES.add("prescriptionPremium");
    EXPENSES.add("medicalAppliances");
    EXPENSES.add("insurancePremiums");
    EXPENSES.add("nursingHome");
    EXPENSES.add("otherMedicalExpenses");
  }

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    var expenses = (List) submission.getInputData().getOrDefault("householdMedicalExpenses[]", emptyList());
    if (!expenses.isEmpty()) {
      int i = 1;
      for (String expense : EXPENSES) {
        var expenseInput = submission.getInputData().get(AMOUNT_PREFIX + expense);
        if (expenseInput != null) {
          results.put("medicalExpensesType" + i, new SingleField("medicalExpensesType", expense, i));
          results.put("medicalExpensesAmount" + i, new SingleField("medicalExpensesAmount", (String) expenseInput, i));
          results.put("medicalExpensesFreq" + i, new SingleField("medicalExpensesFreq", "Monthly", i));
          i++;
        }
      }
    }

    return results;
  }
}
