package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.utils.HouseholdUtilities;
import java.time.LocalDate;
import java.util.Map;

/**
 * This converter will take marital status and age and compute unmarried minor
 */
@NoArgsConstructor
@Slf4j
public class UnmarriedMinorConverter<T, I> extends AbstractBeanField<T, I> {

  @Override
  protected Object convert(String value) {
    return value;
  }

  @Override
  protected String convertToWrite(Object value) {
    Map<String, String> unmarriedMinorInfo = (Map) value;
    var result = false;
    try {
      LocalDate applicantBirthDate = LocalDate.of(
              Integer.parseInt(unmarriedMinorInfo.get("year")),
              Integer.parseInt(unmarriedMinorInfo.get("month")),
              Integer.parseInt(unmarriedMinorInfo.get("day"))
      );
      LocalDate localDate = LocalDate.now();
      if ((applicantBirthDate.isAfter(localDate.minusYears(18L))) && (HouseholdUtilities.unmarriedStatuses.contains(unmarriedMinorInfo.get("maritalStatus")))) {
        result = true;
      }
      return String.valueOf(result);
    } catch (Exception e){
      log.error(e.getMessage());
      return String.valueOf(result);
    }

  }
}

