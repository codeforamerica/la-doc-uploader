package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;
import formflow.library.data.Submission;
import java.util.Map;

@Getter
@Setter
@JsonTypeName("student")
public class StudentCsvModel extends BaseCsvModel {

    @CsvBindByName(column = "active", required=true)
    private Boolean active = true;

    @CsvBindByName(column="zip_code", required=true)
    private String homeAddressZipCode;

    // TODO: see if we need to combine homeAddressStreetAddress1 and StreetAddress2
//    @CsvBindAndJoinByName(column = ".homeAddressStreetAddress*", elementType = String.class)
//    private String homeAddressStreetAddress1;

    @CsvBindByName(column="street_address", required=true)
    private String homeAddressStreetAddress1;

    @CsvBindByName(column="state", required=true)
    private String homeAddressState;

    @CsvBindByName(column="city", required=true)
    private String homeAddressCity;

    @CsvBindByName(column="reference_id")
    private String id;

    @CsvBindByName(column = "first_name", required=true)
    private String firstName; // use id of applicant

    @CsvBindByName(column="last_name", required=true)
    private String lastName; // id of subflow member

    @CsvBindByName(column="birth_date", required=true)
    private String birthDate;

    public static BaseCsvModel generateModel(Submission submission){
        Map<String, Object> inputData = submission.getInputData();
        inputData.put("id", submission.getId());

        return mapper.convertValue(inputData, StudentCsvModel.class);
    }

}
