package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Slf4j
public class ReformatExpensesInsuranceData implements Action {
  @Override
  public void run(Submission submission) {
    Map<String, Object> inputData = submission.getInputData();

    ArrayList<String> expenses = (ArrayList) inputData.get("householdInsuranceExpenses[]");
    Integer numberOfExpense = expenses.size();
    ArrayList<LinkedHashMap> householdExpensesIterator = new ArrayList<>();

    AtomicInteger count = new AtomicInteger(1);
    for(String expense : expenses) {
      LinkedHashMap insuranceExpense = new LinkedHashMap();

      insuranceExpense.put("name", expense);
      insuranceExpense.put("inputName",
          ("medicalExpenseAmount" + formflow.library.inputs.FieldNameMarkers.DYNAMIC_FIELD_MARKER + expense));

      if (count.get() + 1 <= numberOfExpense) {
        insuranceExpense.put("next", expenses.get(count.get()));
        insuranceExpense.put("last", "false");
        count.addAndGet(1);
      } else {
        insuranceExpense.put("last", "true");
      }
      householdExpensesIterator.add(insuranceExpense);
    };

    submission.getInputData().put("householdInsuranceExpensesIterator", householdExpensesIterator);
    submission.getInputData().put("householdInsuranceExpensesCurrent", householdExpensesIterator.get(0));
    submission.getInputData().put("householdInsuranceExpensesComplete", "false");
    submission.getInputData().put("bananas", "wiii");
  }
}
