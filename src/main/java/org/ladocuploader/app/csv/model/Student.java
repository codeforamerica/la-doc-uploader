package org.ladocuploader.app.csv.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {
    @CsvBindByName(column = "first_name")
    private String firstName; // use id of applicant

    @CsvBindByName(column="last_name")
    private String lastName; // id of subflow member

    @CsvBindByName(column="birth_date")
    private String birthDate;

    private String streetAddress;

    private String city;

    private String state;

    private String zipCode;

    @CsvBindByName(column="reference_id")
    private String id;


}
