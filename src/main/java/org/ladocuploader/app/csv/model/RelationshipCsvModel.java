package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.opencsv.bean.CsvBindByName;
import formflow.library.data.Submission;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@JsonTypeName("relationship")
public class RelationshipCsvModel extends BaseCsvModel {

    @CsvBindByName(column = "first_person", required=true)
    private String first_person_id; // use id of applicant

    @CsvBindByName(column="second_person", required=true)
    private String uuid; // id of subflow member

    public static List<BaseCsvModel> generateModel(Submission submission){
        Map<String, Object> inputData = submission.getInputData();
        UUID first_person_id = submission.getId();
        List<BaseCsvModel> relationships = new ArrayList<>();
        List<Map<String, Object>> households = (List<Map<String, Object>>) inputData.get("household");

        if (households != null && !households.isEmpty()) {
            for (Map<String, Object> household : households) {
                Map<String, Object> person = new HashMap<>();
                person.put("first_person_id", first_person_id);
                person.put("uuid", household.get("uuid"));
                relationships.add(mapper.convertValue(person, RelationshipCsvModel.class));
            }
        }

        return relationships;
    }
}
