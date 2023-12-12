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
class ValidateMailingAddressTest {
    private final String BLANK_VALUE="";
    private final String NOT_BLANK_VALUE = "test";
    private final String INVALID_ZIPCODE = "54";
    private final String VALID_ZIPCODE = "12345";

    private final String SAME_AS_HOME_ADDRESS_INPUT_NAME = "sameAsHomeAddress";
    private final String ADDRESS_1_INPUT_NAME = "mailingAddressStreetAddress1";
    private final String CITY_INPUT_NAME = "mailingAddressCity";
    private final String STATE_INPUT_NAME = "mailingAddressState";
    private final String ZIP_INPUT_NAME = "mailingAddressZipCode";

    @Autowired
    private ValidateMailingAddress validator;

    @Test
    public void testAddressHappyPath() {
        FormSubmission form = new FormSubmission(Map.of(
            ADDRESS_1_INPUT_NAME, NOT_BLANK_VALUE,
            CITY_INPUT_NAME, NOT_BLANK_VALUE,
            STATE_INPUT_NAME, NOT_BLANK_VALUE,
            ZIP_INPUT_NAME, VALID_ZIPCODE
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(SAME_AS_HOME_ADDRESS_INPUT_NAME)).isNullOrEmpty();
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
        assertThat(result.get(SAME_AS_HOME_ADDRESS_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(ADDRESS_1_INPUT_NAME)).containsAll(List.of("Make sure you answer this question."));
        assertThat(result.get(CITY_INPUT_NAME)).containsAll(List.of("Make sure you answer this question."));
        assertThat(result.get(STATE_INPUT_NAME)).containsAll(List.of("Make sure you answer this question."));
        assertThat(result.get(ZIP_INPUT_NAME)).containsAll(List.of("Make sure to enter a zip code with 5 digits."));
    }

    @Test
    public void testHomeAddressIsCheckedWhenSameAsHome() {
        FormSubmission form = new FormSubmission(Map.of(
            "sameAsHomeAddress[]", new ArrayList<>(List.of("true")),
            ADDRESS_1_INPUT_NAME, BLANK_VALUE,
            CITY_INPUT_NAME, BLANK_VALUE,
            STATE_INPUT_NAME, BLANK_VALUE,
            ZIP_INPUT_NAME, BLANK_VALUE,
            "homeAddressStreetAddress1", NOT_BLANK_VALUE,
            "homeAddressCity", NOT_BLANK_VALUE,
            "homeAddressState", NOT_BLANK_VALUE,
            "homeAddressZipCode", VALID_ZIPCODE
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(SAME_AS_HOME_ADDRESS_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(ADDRESS_1_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(CITY_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(STATE_INPUT_NAME)).isNullOrEmpty();
        assertThat(result.get(ZIP_INPUT_NAME)).isNullOrEmpty();
    }

}