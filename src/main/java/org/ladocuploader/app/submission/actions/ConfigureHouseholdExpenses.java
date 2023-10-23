package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.dynamic.scaffold.MethodGraph.Linked;
import org.ladocuploader.app.submission.conditions.AbstractSubflowCondition;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Slf4j
public class ConfigureHouseholdExpenses implements Action {

  @Override
  public void run(Submission submission) {
    Map<String, Object> inputData = submission.getInputData();

    ArrayList<String> householdInput = (ArrayList) inputData.get("householdInsuranceExpenses[]");
    boolean currentExpenseIsNotSet = inputData.get("currentExpense") == null;
    listOfPages(householdInput);

    if(currentExpenseIsNotSet){
      inputData.put("currentExpense", listOfPages(householdInput).get(0));
    }

//    if the list has not been set, set the first element of the listOfPages

//    Otherwise, figure out what is next



  }

  private ArrayList<HashMap> listOfPages(ArrayList<String> householdInputs) {
    ArrayList<HashMap> expenses = new ArrayList<>();
    for(int i= 0; i<householdInputs.size(); i++){
      HashMap<String, String> expense = new HashMap<>();
      expense.put("name", householdInputs.get(i));
      expense.put("input", "expenses"+String.join("",householdInputs.get(i).split(" ")));
      if(i+1 >= householdInputs.size()){
        expense.put("nextScreen", null);
      } else {
        expense.put("nextScreen", "expensesInsurance");
      }

      if(i==0){
        expense.put("previousScreen", null);
      } else {
        expense.put("previousScreen", "expensesInsurance");
      }
      expenses.add(expense);
    }

    return expenses;
  }

//  private HashMap<String, String> setNextCurrentPage(HashMap<String, String> currentPage, ArrayList<HashMap> listOfPages){
//
//  }
}