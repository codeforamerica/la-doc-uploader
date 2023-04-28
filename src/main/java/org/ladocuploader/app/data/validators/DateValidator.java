package org.ladocuploader.app.data.validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateValidator implements ConstraintValidator<Date, List<String>> {
  static final DateTimeFormatter MM_DD_YYYY = DateTimeFormatter.ofPattern("M/d/uuuu");

  @Override
  public void initialize(Date constraintAnnotation) {
  }

  @Override
  public boolean isValid(List<String> birthDate, ConstraintValidatorContext context) {
    try {
      LocalDate.parse(String.join("/", birthDate), MM_DD_YYYY);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}
