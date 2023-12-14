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
import java.util.stream.Stream;

import static org.ladocuploader.app.utils.SubmissionUtilities.*;

@Component
public class HouseholdDetailsPreparer implements SubmissionFieldPreparer {


  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    var household = (List<Map<String, Object>>) submission.getInputData().get("household");

    var nonCitizens = (List<String>) submission.getInputData().getOrDefault("nonCitizens[]", new ArrayList<>());
    Map<String, SubmissionField> results = new HashMap<>();
    if (nonCitizens != null && household != null) {
      for (int i = 0; i < household.size(); i++) {
        Map<String, Object> householdMember = household.get(i);
        var uuid = householdMember.get("uuid");
        results.put("householdUSCitizen" + i, new SingleField("householdUSCitizenDerived", nonCitizens.contains(uuid) ? "No" : "Yes", i + 1));
      }
    }

    if (household != null) {
      for (int i = 0; i < household.size(); i++) {
        Map<String, Object> householdMember = household.get(i);
        var educationStatus = householdMember.get("householdMemberHighestEducation");
        results.put("householdHighestEducation" + i, new SingleField("householdHighestEducationFormatted", EDUCATION_MAP.get(educationStatus), i + 1));

        var maritalStatus = householdMember.get("householdMemberMaritalStatus");
        results.put("householdMaritalStatus" + i, new SingleField("householdMaritalStatusFormatted", MARITAL_STATUS_MAP.get(maritalStatus), i + 1));

        var relationshipStatus = householdMember.get("householdMemberRelationship");
        results.put("householdRelationship" + i, new SingleField("householdRelationshipFormatted", RELATIONSHIP_MAP.get(relationshipStatus), i + 1));

        var birthday = Stream.of("householdMemberBirthMonth", "householdMemberBirthDay", "householdMemberBirthYear")
            .map(householdMember::get)
            .reduce((e, c) -> e + "/" + c)
            .get();
        results.put("householdBirthday" + i, new SingleField("householdBirthdayFormatted", (String) birthday, i + 1));
      }
    }


    return results;
  }
}
