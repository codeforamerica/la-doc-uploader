package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.utils.HouseholdUtilities;
import java.time.LocalDate;
import java.util.Map;

/**
 * This converter will take a two line address and concatenate it into one.
 */
@NoArgsConstructor
@Slf4j
public class UnmarriedMinorConverter<T, I> extends AbstractBeanField<T, I> {

  @Override
  protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
    return value;
  }

  @Override
  protected String convertToWrite(Object value) {
    Map<String, String> unmarriedMinorInfo = (Map) value;
    var result = "No";
    try {
      log.info(unmarriedMinorInfo.get("day"));
      log.info(unmarriedMinorInfo.get("month"));
      log.info(unmarriedMinorInfo.get("year"));
      LocalDate applicantBirthDate = LocalDate.of(
              Integer.parseInt(unmarriedMinorInfo.get("year")),
              Integer.parseInt(unmarriedMinorInfo.get("month")),
              Integer.parseInt(unmarriedMinorInfo.get("day"))
      );
      LocalDate localDate = LocalDate.now();
      if ((applicantBirthDate.isAfter(localDate.minusYears(18L))) & (HouseholdUtilities.unmarriedStatuses.contains(unmarriedMinorInfo.get("maritalStatus")))) {
        result = "Yes";
      }
      return result;
    } catch (Exception e){
      log.error(e.getMessage());
      return result;
    }

  }
}

