package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import java.util.Map;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This converter will take a two line address and concatenate it into one.
 */
@NoArgsConstructor
@Slf4j
public class AddressStreetConverter<T, I> extends AbstractBeanField<T, I> {

  @Override
  protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
    return value + "CONVERTED";
  }

  @Override
  protected String convertToWrite(Object value) {
    Map<String, String> address = (Map) value;
    return String.format("%s %s", address.get("address1"), address.get("address2"));
  }
}

