package org.ladocuploader.app.submission.actions;

import static org.assertj.core.api.Assertions.assertThat;

import formflow.library.data.FormSubmission;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest()
@ActiveProfiles("test")
class ValidateHomeAddressTest {
    private final String BLANK_VALUE="";
    private final String NOT_BLANK_VALUE = "test";
    private final String INVALID_ZIPCODE = "54";
    private final String VALID_ZIPCODE = "12345";

    private final String NO_HOME_ADDRESS_INPUT_NAME = "noHomeAddress";
    private final String ADDRESS_1_INPUT_NAME = "homeAddressStreetAddress1";
    private final String CITY_INPUT_NAME = "homeAddressCity";
    private final String STATE_INPUT_NAME = "homeAddressState";
    private final String ZIP_INPUT_NAME = "homeAddressZipCode";

    @Autowired
    private ValidateHomeAddress validator;

    @Test
    public void testAddressHappyPath() {
        FormSubmission form = new FormSubmission(Map.of(
            ADDRESS_1_INPUT_NAME, NOT_BLANK_VALUE,
            CITY_INPUT_NAME, NOT_BLANK_VALUE,
            STATE_INPUT_NAME, NOT_BLANK_VALUE,
            ZIP_INPUT_NAME, VALID_ZIPCODE
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(NO_HOME_ADDRESS_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(ADDRESS_1_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(CITY_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(STATE_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(ZIP_INPUT_NAME)).isNullOrEmpty();
    }
    @Test
    public void testAddressBlankDoesRaiseError() {
        FormSubmission form = new FormSubmission(Map.of(
            ADDRESS_1_INPUT_NAME, BLANK_VALUE,
            CITY_INPUT_NAME, BLANK_VALUE,
            STATE_INPUT_NAME, BLANK_VALUE,
            ZIP_INPUT_NAME, BLANK_VALUE
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(NO_HOME_ADDRESS_INPUT_NAME)).containsAll(List.of("Make sure to provide a home address or select 'I don't have a permanent address'."));
        assertThat(result.get(ADDRESS_1_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(CITY_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(STATE_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(ZIP_INPUT_NAME)).isNullOrEmpty();
    }

    @Test
    public void testAddressBlankDoesNotRaiseErrorWhenUnhoused() {
        FormSubmission form = new FormSubmission(Map.of(
            "noHomeAddress[]", new ArrayList<>(List.of("true")),
            ADDRESS_1_INPUT_NAME, BLANK_VALUE,
            CITY_INPUT_NAME, BLANK_VALUE,
            STATE_INPUT_NAME, BLANK_VALUE,
            ZIP_INPUT_NAME, BLANK_VALUE
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(NO_HOME_ADDRESS_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(ADDRESS_1_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(CITY_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(STATE_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(ZIP_INPUT_NAME)).isNullOrEmpty();
    }

    @Test
    public void testAddressWithMissingValuesRaiseError() {
        FormSubmission form = new FormSubmission(Map.of(
            ADDRESS_1_INPUT_NAME, BLANK_VALUE,
            CITY_INPUT_NAME, BLANK_VALUE,
            STATE_INPUT_NAME, BLANK_VALUE,
            ZIP_INPUT_NAME, INVALID_ZIPCODE
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(NO_HOME_ADDRESS_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(ADDRESS_1_INPUT_NAME)).containsAll(List.of("Make sure you answer this question."));
        assertThat(result.get(CITY_INPUT_NAME)).containsAll(List.of("Make sure you answer this question."));
        assertThat(result.get(STATE_INPUT_NAME)).containsAll(List.of("Make sure you answer this question."));
        assertThat(result.get(ZIP_INPUT_NAME)).containsAll(List.of("Make sure to enter a zip code with 5 digits."));

    }
}