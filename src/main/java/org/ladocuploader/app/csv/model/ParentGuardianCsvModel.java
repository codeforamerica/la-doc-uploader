package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.bean.*;
import formflow.library.data.Submission;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ParentGuardianCsvModel extends BaseCsvModel {

    @CsvBindByName(column = "active", required=true)
    private Boolean active = true;

    @CsvBindByName(column="zip_code", required=true)
    private String homeAddressZipCode;

    // TODO: see if we need to combine homeAddressStreetAddress1 and StreetAddress2
    @CsvBindAndJoinByName(column = ".homeAddressStreetAddress*", elementType = String.class)
    private String homeAddressStreetAddress1;

//    @CsvBindByName(column="street_address", required=true)
//    private String homeAddressStreetAddress1;

    @CsvBindByName(column="state", required=true)
    private String homeAddressState;

    @CsvBindByName(column="city", required=true)
    private String homeAddressCity;

    @CsvBindByName(column="reference_id")
    private String id;

    @CsvBindByName(column = "first_name", required=true)
    private String firstName;

    @CsvBindByName(column = "last_name", required=true)
    private String lastName;

    @CsvBindByName(column = "email_address", required=true)
    private String emailAddress;

    @CsvBindByName(column = "phone_number", required=true)
    private String phoneNumber;

    public static BaseCsvModel generateModel(Submission submission) throws JsonProcessingException {
        Map<String, Object> inputData = submission.getInputData();
        inputData.put("id", submission.getId());

        return mapper.convertValue(inputData, ParentGuardianCsvModel.class);
    }
}
