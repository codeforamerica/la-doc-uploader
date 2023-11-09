package org.ladocuploader.app.csv.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import com.opencsv.bean.CsvBindByName;

@Component
@Getter
@Setter
@NoArgsConstructor
public class ParentGuardian {

    @CsvBindByName(column = "first_name", required = true)
    private String firstName;
    @CsvBindByName(column = "last_name", required = true)
    private String lastName;

    @CsvBindByName(column = "email_address", required = true)
    private String emailAddress;

    @CsvBindByName(column = "phone_number", required = true)
    private String phoneNumber;

//    private Map<String, String> columnMappings = Map.of(
//            "first_name", "firstName",
//            "last_name", "lastName",
//            "email_address", "emailAddress",
//            "phone_number", "phoneNumber"
//    );

}
