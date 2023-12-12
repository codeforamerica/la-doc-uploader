package org.ladocuploader.app.submission.actions;

import formflow.library.data.FormSubmission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
@ActiveProfiles("test")
class ValidateContactMethodTest {


    @Autowired
    private ValidateContactMethod validator;

    @Test
    public void testInvalidPhone() {
        FormSubmission form = new FormSubmission(Map.of(
                "phoneNumber", "123",
                "emailAddress", "",
                "remindersMethod[]", new ArrayList<>(List.of("By Text"))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get("phoneNumber")).containsAll(List.of("Make sure to provide a 9 digit phone number."));

    }

}