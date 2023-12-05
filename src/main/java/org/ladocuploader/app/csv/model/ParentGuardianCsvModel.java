package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.bean.*;
import formflow.library.data.Submission;
import org.ladocuploader.app.csv.converters.AddressStreetConverter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ParentGuardianCsvModel extends BaseCsvModel {

    @CsvBindByName(column = "active", required=true)
    private Boolean active = true;

    @CsvBindByName(column="reference_id")
    private String id;

    @CsvBindByName(column="first_name", required=true)
    private String firstName;

    @CsvBindByName(column="last_name", required=true)
    private String lastName;

    @CsvBindByName(column="email_address", required=true)
    private String emailAddress;

    @CsvBindByName(column="phone_number", required=true)
    private String phoneNumber;

    @CsvCustomBindByName(column="street_address", required=true, converter=AddressStreetConverter.class)
    private List<String> homeAddressStreet = new ArrayList<>();

    @CsvBindByName(column="city", required=true)
    private String homeAddressCity;

    @CsvBindByName(column="state", required=true)
    private String homeAddressState;

    @CsvBindByName(column="zip_code", required=true)
    private String homeAddressZipCode;

    @JsonSetter(value="homeAddressStreetAddress1")
    private void setHomeAddress1(final String address) {
        homeAddressStreet.add(0, address);
    }

    @JsonSetter(value="homeAddressStreetAddress2")
    private void setHomeAddress2(final String address) {
        homeAddressStreet.add(1, address);
    }

    public static BaseCsvModel generateModel(Submission submission) throws JsonProcessingException {
        Map<String, Object> inputData = submission.getInputData();
        inputData.put("id", submission.getId());

        return mapper.convertValue(inputData, ParentGuardianCsvModel.class);
    }
}
