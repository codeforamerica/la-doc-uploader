package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.bean.*;
import formflow.library.data.Submission;
import java.util.HashMap;

import org.ladocuploader.app.csv.CsvBindByNameOrder;
import org.ladocuploader.app.csv.converters.AddressStreetConverter;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import org.ladocuploader.app.csv.converters.PhoneNumberConverter;

@Getter
@Setter
@CsvBindByNameOrder({"active","city","email_address","first_name","last_name","phone_number","reference_id","state","street_address","zip_code"})
public class ParentGuardianCsvModel extends BaseCsvModel {

    @CsvBindByName(column = "active")
    private Boolean active = true;

    @CsvBindByName(column="reference_id")
    private String id;

    @CsvBindByName(column="first_name")
    private String firstName;

    @CsvBindByName(column="last_name")
    private String lastName;

    @CsvBindByName(column="email_address")
    private String emailAddress;

    @CsvCustomBindByName(column="phone_number", converter = PhoneNumberConverter.class)
    private String phoneNumber;

    @CsvCustomBindByName(column="street_address", converter = AddressStreetConverter.class)
    private Map<String, String> homeAddressStreet = new HashMap<>();

    @CsvBindByName(column="city")
    private String homeAddressCity;

    @CsvBindByName(column="state")
    private String homeAddressState;

    @CsvBindByName(column="zip_code")
    private String homeAddressZipCode;

    @JsonSetter(value="homeAddressStreetAddress1")
    private void setHomeAddress1(final String address) {
        if (address != null) {
            homeAddressStreet.put("address1", address);
        }
    }

    @JsonSetter(value="homeAddressStreetAddress2")
    private void setHomeAddress2(final String address) {
        if (address != null) {
            homeAddressStreet.put("address2", address);
        }
    }

    public static BaseCsvModel generateModel(Submission submission) throws JsonProcessingException {
        Map<String, Object> inputData = submission.getInputData();
        inputData.put("id", submission.getId());
        // TODO: check if they have a child who is eligible (or family member is pregnant)
        ParentGuardianCsvModel pgpModel = mapper.convertValue(inputData, ParentGuardianCsvModel.class);
        pgpModel.setSubmissionId(submission.getId());
        return pgpModel;
    }
}
