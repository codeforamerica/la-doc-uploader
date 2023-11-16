package org.ladocuploader.app.csv.model;

import com.opencsv.bean.CsvCustomBindByName;
import lombok.Getter;
import lombok.Setter;
import com.opencsv.bean.CsvBindByName;
import org.ladocuploader.app.csv.converters.ComputedFieldConverter;

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

    @CsvCustomBindByName(column="computed", converter = ComputedFieldConverter.class)
    private String whosApplying;

}
