package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    Map<String, Boolean> unmarriedMinorInfo = (Map) value;
    var result = "No";
    if ((unmarriedMinorInfo.get("isMinor")) & (unmarriedMinorInfo.get("isUnmarried"))){
      result = "Yes";
    }
    return result;
  }
}

