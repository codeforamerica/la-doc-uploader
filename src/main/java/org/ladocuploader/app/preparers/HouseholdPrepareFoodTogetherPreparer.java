package org.ladocuploader.app.preparers;

import static java.util.Collections.emptyList;
import static org.ladocuploader.app.utils.StringUtilities.joinWithCommaAndSpace;
import static org.ladocuploader.app.utils.SubmissionUtilities.getHouseholdMemberNames;
import static org.ladocuploader.app.utils.SubmissionUtilities.householdMemberFullName;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Component
public class HouseholdPrepareFoodTogetherPreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> result = new HashMap<>();
    var household = (List<Map<String, Object>>) submission.getInputData().getOrDefault("household", emptyList());
    List<String> preparerFoodTogetherUUIDs = (List<String>) submission.getInputData().getOrDefault("preparesFood[]", emptyList());
    List<String> householdMembersWhoDoNotPrepareFoodTogether = new ArrayList<>();
    
    if (household.isEmpty()) {
      return result;
    }
    
    if (preparerFoodTogetherUUIDs.isEmpty()) {
      householdMembersWhoDoNotPrepareFoodTogether.addAll(getHouseholdMemberNames(submission));
      
      result.put("preparesFoodNames", 
          new SingleField("preparesFoodNames", joinWithCommaAndSpace(householdMembersWhoDoNotPrepareFoodTogether), null));
      return result;
    }
    
    for (var member : household) {
      var uuid = (String) member.get("uuid");
      if (!preparerFoodTogetherUUIDs.contains(uuid)) {
        householdMembersWhoDoNotPrepareFoodTogether.add(householdMemberFullName(member));
      }
    }
    
    result.put("preparesFoodNames", 
        new SingleField("preparesFoodNames", joinWithCommaAndSpace(householdMembersWhoDoNotPrepareFoodTogether), null));
    
    return result;
  }
}
