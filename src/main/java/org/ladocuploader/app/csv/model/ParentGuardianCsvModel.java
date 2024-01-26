package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.bean.*;
import formflow.library.data.Submission;
import java.util.HashMap;

import org.ladocuploader.app.csv.CsvBindByNameOrder;
import org.ladocuploader.app.csv.converters.AddressStreetConverter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import org.ladocuploader.app.csv.converters.PhoneNumberConverter;

@Getter
@Setter
@CsvBindByNameOrder({"first_name","last_name", "phone_number", "active", "reference_id", "email_address", "street_address", "zip_code"})
public class ParentGuardianCsvModel extends BaseCsvModel {

    @CsvBindByName(column = "active")
    @CsvBindByPosition(position=0)
    private Boolean active = true;

    @CsvBindByName(column="reference_id")
    @CsvBindByPosition(position=1)
    private String id;

    @CsvBindByName(column="first_name")
    @CsvBindByPosition(position=2)
    private String firstName;

    @CsvBindByName(column="last_name")
    @CsvBindByPosition(position=3)
    private String lastName;

    @CsvBindByName(column="email_address")
    @CsvBindByPosition(position=4)
    private String emailAddress;

    @CsvCustomBindByName(column="phone_number", converter= PhoneNumberConverter.class)
    @CsvBindByPosition(position=5)
    private String phoneNumber;

    @CsvCustomBindByName(column="street_address", converter=AddressStreetConverter.class)
    @CsvBindByPosition(position=6)
    private Map<String, String> homeAddressStreet = new HashMap<>();

    @CsvBindByName(column="city")
    @CsvBindByPosition(position=7)
    private String homeAddressCity;

    @CsvBindByName(column="state")
    @CsvBindByPosition(position=8)
    private String homeAddressState;

    @CsvBindByName(column="zip_code")
    @CsvBindByPosition(position=9)
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

        ParentGuardianCsvModel pgpModel = mapper.convertValue(inputData, ParentGuardianCsvModel.class);
        pgpModel.setSubmissionId(submission.getId());
        return pgpModel;
    }
}
