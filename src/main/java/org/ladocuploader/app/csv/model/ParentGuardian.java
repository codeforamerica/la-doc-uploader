package org.ladocuploader.app.csv.model;

import com.opencsv.bean.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.ladocuploader.app.csv.converters.InputDataConverter;

import java.util.Map;

@Getter
@Setter
public class ParentGuardian {

    @CsvBindByName(column = "first_name")
    private String firstName;
    @CsvBindByName(column = "last_name")
    private String lastName;

    @CsvBindByName(column = "email_address")
    private String emailAddress;

    @CsvBindByName(column = "phone_number")
    private String phoneNumber;
    @CsvBindByName(column = "active")
    private String active = "true";

    @CsvBindByName(column="zip_code")
    private String homeAddressZipCode;

    // TODO: see if we need to combine homeAddressStreetAddress1 and StreetAddress2
//    @CsvBindAndJoinByName(column = ".homeAddressStreetAddress*", elementType = String.class)
//    private String homeAddressStreetAddress1;

    @CsvBindByName(column="street_address")
    private String homeAddressStreetAddress1;

    @CsvBindByName(column="state")
    private String homeAddressState;

    @CsvBindByName(column="city")
    private String homeAddressCity;

    @CsvBindByName(column="reference_id")
    private String id;


}
