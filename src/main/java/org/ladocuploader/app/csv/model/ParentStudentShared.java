package org.ladocuploader.app.csv.model;

import com.opencsv.bean.CsvBindByName;

public class ParentStudentShared extends BaseCsvModel {

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
}
