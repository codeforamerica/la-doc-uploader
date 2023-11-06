package org.ladocuploader.app.csv.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ParentGuardian {

    private String first_name;
    private String last_name;

    private String email_address;

    private String phone_number;

}
