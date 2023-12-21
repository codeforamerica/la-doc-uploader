package org.ladocuploader.app.data.validators;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;


import static org.ladocuploader.app.data.validators.DateValidator.MM_DD_YYYY;

@Slf4j
public class DateRangeValidator implements ConstraintValidator<DateWithinRange, List<String>> {

  static final LocalDate MIN_DATE = LocalDate.of(1900, 1, 1);

  @Override
  public void initialize(DateWithinRange constraintAnnotation) {
  }

  @Override
  public boolean isValid(List<String> birthDate, ConstraintValidatorContext context) {
    try {
      return validRange(LocalDate.parse(String.join("/", birthDate), MM_DD_YYYY));
    } catch (DateTimeParseException e) {
      // Eat this exception - it's handled by another validator
    }
    return true;
  }

  private static boolean validRange(LocalDate inputDate) {
    return inputDate.isBefore(LocalDate.now()) && inputDate.isAfter(MIN_DATE);
  }
}
