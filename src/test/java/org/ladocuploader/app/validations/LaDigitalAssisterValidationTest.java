package org.ladocuploader.app.validations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.LaDigitalAssisterTestBuilder;
import org.ladocuploader.app.inputs.LaDigitalAssister;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class LaDigitalAssisterValidationTest {

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    private LaDigitalAssister laDigitalAssister;

    @BeforeEach
    public void setUp() {
        // Set up a valid DA object
        laDigitalAssister = new LaDigitalAssisterTestBuilder()
                .validSinglePersonalHouseholdSNAP()
                .build();
    }

    @Test
    public void testInvalidPhoneNumber() {
        laDigitalAssister.setPhoneNumber("Jackyahoo.com");
        Set<ConstraintViolation<LaDigitalAssister>> violations = validator.validate(laDigitalAssister);

        Map<String, String> violationMap = new HashMap<>();
        for(ConstraintViolation<LaDigitalAssister> violation : violations) {
            violationMap.put(violation.getPropertyPath().toString(), violation.getMessageTemplate());
        }

        assertThat(violationMap.get("phoneNumber")).isEqualTo("{error.phone}");
    }
}