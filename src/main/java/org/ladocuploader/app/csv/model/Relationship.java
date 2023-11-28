package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvBindByName;
import formflow.library.data.Submission;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Relationship extends BaseCsvModel {

    @CsvBindByName(column = "first_person", required=true)
    private String first_person_id; // use id of applicant
    @CsvBindByName(column="second_person", required=true)
    private String uuid; // id of subflow member

    public static List<BaseCsvModel> generateModel(Submission submission){
        Map<String, Object> inputData = submission.getInputData();
        UUID first_person_id = submission.getId();
        List<BaseCsvModel> relationships = new ArrayList<>();
        List<Map<String, Object>> households = (List<Map<String, Object>>) inputData.get("household");
        for (Map<String, Object> household : households){
            household.put("first_person_id", first_person_id);
            relationships.add(mapper.convertValue(household, Relationship.class));
        }
        return relationships;
    }
}
