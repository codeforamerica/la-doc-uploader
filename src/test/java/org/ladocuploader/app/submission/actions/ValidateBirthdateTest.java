package org.ladocuploader.app.submission.actions;

import static org.assertj.core.api.Assertions.assertThat;

import formflow.library.data.FormSubmission;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest()
@ActiveProfiles("test")
class ValidateBirthdateTest {
    private final String BLANK_VALUE="";
    private final String BIRTHDATE_INPUT_NAME = "birthDay";

    @Autowired
    private ValidateBirthdate validator;

    @Test
    public void testBirthDateBlankDoesNotRaiseAnError() {
        FormSubmission form = new FormSubmission(Map.of(
            "birthMonth", BLANK_VALUE,
            "birthDay", BLANK_VALUE,
            "birthYear",BLANK_VALUE
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(BIRTHDATE_INPUT_NAME)).isNullOrEmpty();
    }

    @Test
    public void testBirthDayWrongFormatDoesNotRaiseAnError() {
        FormSubmission form = new FormSubmission(Map.of(
            "birthMonth", "01",
            "birthDay", "1",
            "birthYear","1991"
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(BIRTHDATE_INPUT_NAME)).isNullOrEmpty();
    }

    @Test
    public void testBirthMonthWrongFormatDoesNotRaiseAnError() {
        FormSubmission form = new FormSubmission(Map.of(
            "birthMonth", "1",
            "birthDay", "01",
            "birthYear","1991"
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(BIRTHDATE_INPUT_NAME)).isNullOrEmpty();
    }


    @Test
    public void testBirthYearWrongFormatDoesRaiseAnError() {
        FormSubmission form = new FormSubmission(Map.of(
            "birthMonth", "01",
            "birthDay", "01",
            "birthYear","91"
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(BIRTHDATE_INPUT_NAME)).containsAll(List.of("Make sure to provide a date in MM/DD/YYYY OR M/D/YYYY."));
    }

    @Test
    public void testBirthYearBefore1900sDoesRaiseAnError() {
        FormSubmission form = new FormSubmission(Map.of(
            "birthMonth", "01",
            "birthDay", "01",
            "birthYear","1885"
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(BIRTHDATE_INPUT_NAME)).containsAll(List.of("Make sure to provide a valid date."));
    }

    @Test
    public void testBirthDateInFutureDoesRaiseAnError() {
        FormSubmission form = new FormSubmission(Map.of(
            "birthMonth", "01",
            "birthDay", "01",
            "birthYear","2027"
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(BIRTHDATE_INPUT_NAME)).containsAll(List.of("Make sure to provide a valid date."));
    }

    @Test
    public void testBirthdayHappyPath() {
        FormSubmission form = new FormSubmission(Map.of(
            "birthMonth", "1",
            "birthDay", "1",
            "birthYear","1991"
        ));

        Map<String, List<String>> result = validator.runValidation(form, null);
        assertThat(result.get(BIRTHDATE_INPUT_NAME)).isNullOrEmpty();
    }

}