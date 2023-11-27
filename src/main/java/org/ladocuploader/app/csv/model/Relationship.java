package org.ladocuploader.app.csv.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Relationship {

    @CsvBindByName(column = "first_person")
    private String first_person_id; // use id of applicant
    @CsvBindByName(column="second_person")
    private String uuid; // id of subflow member
}
