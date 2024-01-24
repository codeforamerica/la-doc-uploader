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
    private final String WANTS_REMINDER_INPUT_NAME="wantsReminders[]";
    private final String BLANK_VALUE="";
    private final String PHONE_NUMBER_INPUT_NAME = "phoneNumber";
    private final String CELL_NUMBER_INPUT_NAME = "cellPhoneNumber";
    private final String WORK_NUMBER_INPUT_NAME = "workPhoneNumber";

    @Autowired
    private ValidateContactMethod validator;

    @Test
    public void testMainPhoneHappyPath() {
        FormSubmission form = new FormSubmission(Map.of(
            PHONE_NUMBER_INPUT_NAME, VALID_PHONE,
            CELL_NUMBER_INPUT_NAME, BLANK_VALUE,
            WORK_NUMBER_INPUT_NAME, BLANK_VALUE,
            WANTS_REMINDER_INPUT_NAME, new ArrayList<>(List.of(true))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(PHONE_NUMBER_INPUT_NAME)).isNullOrEmpty();
    }

    @Test
    public void testCellPhoneHappyPath() {
        FormSubmission form = new FormSubmission(Map.of(
            PHONE_NUMBER_INPUT_NAME, BLANK_VALUE,
            CELL_NUMBER_INPUT_NAME, VALID_PHONE,
            WORK_NUMBER_INPUT_NAME, BLANK_VALUE,
            WANTS_REMINDER_INPUT_NAME, new ArrayList<>(List.of(true))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(CELL_NUMBER_INPUT_NAME)).isNullOrEmpty();
    }

    @Test
    public void testWorkPhoneHappyPath() {
        FormSubmission form = new FormSubmission(Map.of(
            PHONE_NUMBER_INPUT_NAME, BLANK_VALUE,
            CELL_NUMBER_INPUT_NAME, BLANK_VALUE,
            WORK_NUMBER_INPUT_NAME, VALID_PHONE,
            WANTS_REMINDER_INPUT_NAME, new ArrayList<>(List.of(true))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(WORK_NUMBER_INPUT_NAME)).isNullOrEmpty();
    }

    @Test
    public void doesNotRaiseErrorIfDoesNotWantReminder() {
        FormSubmission form = new FormSubmission(Map.of(
            PHONE_NUMBER_INPUT_NAME, BLANK_VALUE,
            CELL_NUMBER_INPUT_NAME, BLANK_VALUE,
            WORK_NUMBER_INPUT_NAME, BLANK_VALUE,
            WANTS_REMINDER_INPUT_NAME, new ArrayList<>(List.of(false))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(PHONE_NUMBER_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(CELL_NUMBER_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(WORK_NUMBER_INPUT_NAME)).isNullOrEmpty();
    }

    @Test
    public void raisesErrorIfWantsReminderButNoInputs() {
        FormSubmission form = new FormSubmission(Map.of(
            PHONE_NUMBER_INPUT_NAME, BLANK_VALUE,
            CELL_NUMBER_INPUT_NAME, BLANK_VALUE,
            WORK_NUMBER_INPUT_NAME, BLANK_VALUE,
            WANTS_REMINDER_INPUT_NAME, new ArrayList<>(List.of(true))
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(PHONE_NUMBER_INPUT_NAME)).containsAll(List.of("Make sure to provide a 9 digit phone number."));
        assertThat(result.get(CELL_NUMBER_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(WORK_NUMBER_INPUT_NAME)).isNullOrEmpty();
    }

}