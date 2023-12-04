package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddressStreetConverter<T, I> extends AbstractBeanField<T, I> {

  public AddressStreetConverter() {
    log.info("Creating AddressStreetConverter");
  }
  @Override
  protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
    log.info("converting {}", value);
    return value + "CONVERTED";
  }

  @Override
  protected String convertToWrite(Object value) {
    log.info("converting to write {}", value);
    List<String> address = (List) value;
    return String.format("%s %s", address.get(0), address.get(1));
  }
}

