package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.*;
import formflow.library.data.Submission;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ParentGuardian extends ParentStudentShared {

    @CsvBindByName(column = "first_name", required=true)
    private String firstName;
    @CsvBindByName(column = "last_name", required=true)
    private String lastName;

    @CsvBindByName(column = "email_address", required=true)
    private String emailAddress;

    @CsvBindByName(column = "phone_number", required=true)
    private String phoneNumber;

    public static BaseCsvModel generateModel(Submission submission){
        Map<String, Object> inputData = submission.getInputData();
        inputData.put("id", submission.getId());

        return mapper.convertValue(inputData, ParentGuardian.class);
    }


}
