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
public class HouseholdResourcesPreparer implements SubmissionFieldPreparer {
  private static final String AMOUNT_PREFIX = "moneyOnHandValue_wildcard_";
  private static final String OWNER_PREFIX = "moneyOnHandOwner_wildcard_";
  private static final List<String> RESOURCES = new ArrayList<>();

  static {
    RESOURCES.add("Checking account");
    RESOURCES.add("Savings account");
    RESOURCES.add("Joint account");
    RESOURCES.add("Bonds");
    RESOURCES.add("Cash on hand");
    RESOURCES.add("Certificate of Deposit (CD)");
    RESOURCES.add("Money Market Account");
    RESOURCES.add("Mutual funds");
    RESOURCES.add("Savings bond");
    RESOURCES.add("Stocks");
  }

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    var types = (List) submission.getInputData().getOrDefault("moneyOnHandTypes[]", emptyList());
    if (!types.isEmpty()) {
      List<String> sortedResources = RESOURCES.stream().sorted().toList();
      int i = 1;
      for (String expense : sortedResources) {
        var amount = submission.getInputData().get(AMOUNT_PREFIX + expense);
        if (amount != null) {
          var owner = submission.getInputData().get(OWNER_PREFIX + expense);
          results.put("moneyOnHandType" + i, new SingleField("moneyOnHandType", expense, i));
          results.put("moneyOnHandAmount" + i, new SingleField("moneyOnHandAmount", (String) amount, i));
          results.put("moneyOnHandOwner" + i, new SingleField("moneyOnHandOwner", (String) owner, i));
          i++;
        }
      }
    }

    return results;
  }
}
