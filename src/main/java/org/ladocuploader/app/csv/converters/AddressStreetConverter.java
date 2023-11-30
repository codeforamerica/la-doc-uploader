package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class AddressStreetConverter<T, I> extends AbstractBeanField<T, I> {

  @Override
  protected Object convert(String value) {
    log.info("converting {}", value);
    return "CONVERTED";
  }

  @Override
  protected String convertToWrite(Object value) {
    log.info("converting to write {}", value);
    List<String> address = (List) value;
    return String.format("%s %s", address.get(0), address.get(1));
  }
}

