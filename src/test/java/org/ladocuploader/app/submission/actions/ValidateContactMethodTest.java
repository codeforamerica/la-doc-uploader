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

    private final String VALID_PHONE="(123) 459-0392";
    private final String VALID_EMAIL="test@mail.com";
    private final String EMAIL_REMINDER="By Email";
    private final String PHONE_REMINDER="By Text";
    private final String BLANK_VALUE="";
    private final String PHONE_NUMBER_INPUT_NAME = "phoneNumber";
    private final String EMAIL_ADDRESS_INPUT_NAME = "emailAddress";

    @Autowired
    private ValidateContactMethod validator;

    @Test
    public void testPhoneInvalidRaisesAnError() {
        FormSubmission form = new FormSubmission(Map.of(
                "phoneNumber", "1234561234",
                "emailAddress", BLANK_VALUE,
                "remindersMethod[]", new ArrayList<>(List.of(PHONE_REMINDER))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(PHONE_NUMBER_INPUT_NAME)).containsAll(List.of("Make sure to provide a 9 digit phone number."));
    }

    @Test
    public void testPhoneMissingDoesNotRaiseErrorWhenPhoneNotExpected() {
        FormSubmission form = new FormSubmission(Map.of(
            "phoneNumber", BLANK_VALUE,
            "emailAddress", VALID_EMAIL,
            "remindersMethod[]", new ArrayList<>(List.of(EMAIL_REMINDER))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(PHONE_NUMBER_INPUT_NAME)).isNullOrEmpty();
    }

    @Test
    public void testPhoneMissingRaisesErrorWhenPhoneExpected() {
        FormSubmission form = new FormSubmission(Map.of(
            "phoneNumber", BLANK_VALUE,
            "emailAddress", VALID_EMAIL,
            "remindersMethod[]", new ArrayList<>(List.of(PHONE_REMINDER))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(PHONE_NUMBER_INPUT_NAME)).containsAll(List.of("Make sure to provide a 9 digit phone number."));
    }

    @Test
    public void testPhoneHappyPath() {
        FormSubmission form = new FormSubmission(Map.of(
            "phoneNumber", VALID_PHONE,
            "emailAddress", VALID_EMAIL,
            "remindersMethod[]", new ArrayList<>(List.of(PHONE_REMINDER))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(PHONE_NUMBER_INPUT_NAME)).isNullOrEmpty();
    }

    @Test
    public void testEmailInvalidRaisesAnError() {
        FormSubmission form = new FormSubmission(Map.of(
            "phoneNumber", BLANK_VALUE,
            "emailAddress", "mail.com",
            "remindersMethod[]", new ArrayList<>(List.of(EMAIL_REMINDER))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(EMAIL_ADDRESS_INPUT_NAME)).containsAll(List.of("Make sure to provide a valid email address."));
    }

    @Test
    public void testEmailMissingDoesNotRaiseErrorWhenEmailNotExpected() {
        FormSubmission form = new FormSubmission(Map.of(
            "phoneNumber", VALID_PHONE,
            "emailAddress", BLANK_VALUE,
            "remindersMethod[]", new ArrayList<>(List.of(PHONE_REMINDER))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(EMAIL_ADDRESS_INPUT_NAME)).isNullOrEmpty();
    }

    @Test
    public void testEmailMissingRaisesErrorWhenEmailExpected() {
        FormSubmission form = new FormSubmission(Map.of(
            "phoneNumber", VALID_PHONE,
            "emailAddress", BLANK_VALUE,
            "remindersMethod[]", new ArrayList<>(List.of(EMAIL_REMINDER))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(EMAIL_ADDRESS_INPUT_NAME)).containsAll(List.of("Make sure to provide a valid email address."));
    }

    @Test
    public void testEmailHappyPath() {
        FormSubmission form = new FormSubmission(Map.of(
            "phoneNumber", VALID_PHONE,
            "emailAddress", VALID_EMAIL,
            "remindersMethod[]", new ArrayList<>(List.of(EMAIL_REMINDER))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(EMAIL_ADDRESS_INPUT_NAME)).isNullOrEmpty();
    }

}