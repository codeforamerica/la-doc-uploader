package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.utils.SubmissionUtilities;
import org.springframework.stereotype.Component;

import java.util.*;

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

    var householdMemberExpenses = (List<Map<String, Object>>) submission.getInputData().getOrDefault("householdMedical", emptyList());

    if (!householdMemberExpenses.isEmpty()) {
      int i = 1;
      for (Map<String, Object> householdMember: householdMemberExpenses){
        String memberUUID = (String) householdMember.get("medicalExpenseMember");
        String memberFullname = SubmissionUtilities.getHouseholdMemberFullnameByUUID(memberUUID, submission.getInputData());
        List<String> medicalExpenses = (List<String>) householdMember.get("householdMedicalExpenses[]");
        for (String expense : medicalExpenses){
          var amount = householdMember.get(AMOUNT_PREFIX + expense);
          results.put("medicalExpensesPerson" + i, new SingleField("medicalExpensesPerson", memberFullname, i));
          results.put("medicalExpensesAmount" + i, new SingleField("medicalExpensesAmount", (String) amount, i));
          results.put("medicalExpensesType" + i, new SingleField("medicalExpensesType", EXPENSES.get(expense), i));
          results.put("medicalExpensesFreq" + i, new SingleField("medicalExpensesFreq", "Monthly", i));
          i++;
        }
      }
    }

    return results;
  }
}
