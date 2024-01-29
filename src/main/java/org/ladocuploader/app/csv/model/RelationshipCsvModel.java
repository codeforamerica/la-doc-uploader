package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.opencsv.bean.CsvBindByName;
import formflow.library.data.Submission;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import org.ladocuploader.app.csv.CsvBindByNameOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.ladocuploader.app.utils.HouseholdUtilities;

@Getter
@Setter
@JsonTypeName("relationship")
@CsvBindByNameOrder({"first_person", "second_person"})
public class RelationshipCsvModel extends BaseCsvModel {

    @CsvBindByName(column = "first_person", required=true)
    private String first_person_id; // id of applicant

    @CsvBindByName(column="second_person", required=true)
    private String uuid; // id of subflow member

    public static List<BaseCsvModel> generateModel(Submission submission){
        Map<String, Object> inputData = submission.getInputData();
        // TODO: Do we want to use submission application id here, or the applicant's 'uuid' that is assigned?
        // Having it be the submission id makes it easier for us to debug, if need be.
        UUID first_person_id = submission.getId();
        List<BaseCsvModel> relationships = new ArrayList<>();
        List<Map<String, Object>> households = (List<Map<String, Object>>) inputData.get("household");

        if (households != null && !households.isEmpty()) {
            for (Map<String, Object> member : households) {
                // TODO: include check to see if someone in household is pregnant
                if (!HouseholdUtilities.isMemberEceEligible(member, inputData)) {
                    continue;
                }

                Map<String, Object> person = new HashMap<>();
                person.put("first_person_id", first_person_id);
                person.put("uuid", member.get("uuid"));
                RelationshipCsvModel relModel = mapper.convertValue(person, RelationshipCsvModel.class);
                relModel.setSubmissionId(submission.getId());
                relationships.add(relModel);
            }
        }

        return relationships;
    }
}
