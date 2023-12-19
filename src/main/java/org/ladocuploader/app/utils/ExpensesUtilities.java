package org.ladocuploader.app.utils;

import formflow.library.data.Submission;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExpensesUtilities {
  public static LinkedHashMap firstExpense(Submission submission) {
    Map<String, Object> inputData = submission.getInputData();

    ArrayList<LinkedHashMap> allExpenses = (ArrayList<LinkedHashMap>) inputData.get("householdInsuranceExpensesIterator[]");

    return allExpenses.get(0);
  };

  public static LinkedHashMap currentExpense(Submission submission, String parameterIndex) {
    Map<String, Object> inputData = submission.getInputData();

    if(parameterIndex == null){
      return firstExpense(submission);
    } else {
      Integer index = Integer.parseInt(parameterIndex);

      ArrayList<LinkedHashMap> allExpenses = (ArrayList<LinkedHashMap>) inputData.get("householdInsuranceExpensesIterator[]");

      return allExpenses.get(index);
    }
  };
}
