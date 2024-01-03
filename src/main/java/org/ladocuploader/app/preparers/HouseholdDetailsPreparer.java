package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.inputs.FieldNameMarkers;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import java.util.stream.Collectors;
import org.ladocuploader.app.data.enums.EthnicityType;
import org.ladocuploader.app.data.enums.RaceType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.ladocuploader.app.utils.SubmissionUtilities.*;

@Component
public class HouseholdDetailsPreparer implements SubmissionFieldPreparer {
  private static final String RACE_PREFIX = "householdMemberRace" + FieldNameMarkers.DYNAMIC_FIELD_MARKER;
  private static final String ETHNICITY_PREFIX = "householdMemberEthnicity" + FieldNameMarkers.DYNAMIC_FIELD_MARKER;

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    var nonCitizens = (List<String>) submission.getInputData().getOrDefault("nonCitizens[]", new ArrayList<>());
    var household = (List<Map<String, Object>>) submission.getInputData().get("household");
    if (household != null) {
      for (int i = 0; i < household.size(); i++) {
        Map<String, Object> householdMember = household.get(i);
        var educationStatus = householdMember.get("householdMemberHighestEducation");
        results.put("householdHighestEducation" + i, new SingleField("householdHighestEducationFormatted", PDF_EDUCATION_MAP.get(educationStatus), i + 1));

        var maritalStatus = householdMember.get("householdMemberMaritalStatus");
        results.put("householdMaritalStatus" + i, new SingleField("householdMaritalStatusFormatted", PDF_MARITAL_STATUS_MAP.get(maritalStatus), i + 1));

        var relationshipStatus = householdMember.get("householdMemberRelationship");
        results.put("householdRelationship" + i, new SingleField("householdRelationshipFormatted", PDF_RELATIONSHIP_MAP.get(relationshipStatus), i + 1));

        var birthday = Stream.of("householdMemberBirthMonth", "householdMemberBirthDay", "householdMemberBirthYear")
            .map(householdMember::get)
            .reduce((e, c) -> e + "/" + c)
            .get();
        results.put("householdBirthday" + i, new SingleField("householdBirthdayFormatted", (String) birthday, i + 1));

        var uuid = householdMember.get("uuid");
        results.put("householdUSCitizen" + i, new SingleField("householdUSCitizenDerived", nonCitizens != null && nonCitizens.contains(uuid) ? "No" : "Yes", i + 1));

        List<String> raceInput = (List)submission.getInputData().get(RACE_PREFIX + uuid + "[]");
        String ethnicityInput = (String) submission.getInputData().get(ETHNICITY_PREFIX + uuid);
        String raceEthnicCode = "";

        if (raceInput != null && !raceInput.isEmpty()) {
          raceEthnicCode = raceInput.stream()
              .map(t -> RaceType.getAbbreviationFromValue(t))
              // 'AN' maps to both American Indian and Alaskan Native, if both are chosen, only keep one 'AN'
              .distinct()
              .collect(Collectors.joining(","));
        }

        if (ethnicityInput != null && !ethnicityInput.isEmpty()) {
          raceEthnicCode += "/" + EthnicityType.getAbbreviationFromValue(ethnicityInput);
        }
        results.put("householdRaceEthnicCode" + i, new SingleField("householdRaceEthnicCode", raceEthnicCode, i+1));
      }
    }
    return results;
  }
}
