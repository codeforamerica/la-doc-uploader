package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Collections.emptyList;

@Component
public class AdditionalIncomeDetailsPreparer implements SubmissionFieldPreparer {

  private static final String AMOUNT_PREFIX = "additionalIncomeValue_wildcard_";
  private static final String OWNER_PREFIX = "additionalIncomeProvider_wildcard_";

  private static final String FREQUENCY_PREFIX = "additionalIncomeFrequency_wildcard_";

  private static final String EXPECTED_RUN_OUT_PREFIX = "additionalIncomeExpectRunOut_wildcard_";

  private static final String EXPECTED_END_DATE_PREFIX = "additionalIncomeExpectedEndDate_wildcard_";
  private static final List<String> RESOURCES = new ArrayList<>();

  static {
    RESOURCES.add("SSI");
    RESOURCES.add("SocialSecurity");
    RESOURCES.add("Unemployment");
    RESOURCES.add("WorkersCompensation");
    RESOURCES.add("ChildSupport");
    RESOURCES.add("VeteransBenefits");
    RESOURCES.add("Contributions");
  }

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    var types = (List) submission.getInputData().getOrDefault("additionalIncome[]", emptyList());
    if (!types.isEmpty()) {
      int i = 1;
      for (String expense : RESOURCES) {
        var amount = submission.getInputData().get(AMOUNT_PREFIX + expense);
        var frequency = submission.getInputData().get(FREQUENCY_PREFIX + expense);
        var expectedToRunOut = submission.getInputData().get(EXPECTED_RUN_OUT_PREFIX + expense);
        var expectedEndDate = submission.getInputData().get(EXPECTED_END_DATE_PREFIX + expense);
        if (amount != null) {
          var owner = getHouseholdResourceOwner(submission.getInputData(), OWNER_PREFIX+expense);
          results.put("additionalIncomeOwner" + i, new SingleField("additionalIncomeOwner", owner, i));
          results.put("additionalIncomeType" + i, new SingleField("additionalIncomeType", expense, i));
          results.put("additionalIncomeAmount" + i, new SingleField("additionalIncomeAmount", (String) amount, i));
          results.put("additionalIncomeFrequency" + i, new SingleField("additionalIncomeFrequency", (String) frequency, i));
          results.put("additionalIncomeExpectedToRunOut" + i, new SingleField("additionalIncomeExpectedToRunOut", expectedToRunOut.equals("true") ? "true" : "false", i));
          results.put("additionalIncomeExpectedEndDate" + i, new SingleField("additionalIncomeEndDate", expectedToRunOut.equals("true") ? (String) expectedEndDate : "", i));
          i++;
        }
      }
    }

    return results;
  }

  private static String getHouseholdResourceOwner(Map<String, Object> inputData, String key) {
    var owner = inputData.get(key);
    if("you".equals(owner)) {
      return "%s %s".formatted(inputData.get("firstName"), inputData.get("lastName"));
    }
    return (String) owner;
  }
}
