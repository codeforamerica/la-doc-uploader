package org.ladocuploader.app.csv.model;

import com.opencsv.bean.CsvIgnore;
import formflow.library.data.Submission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import com.opencsv.bean.CsvBindByName;

@Component
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

}
