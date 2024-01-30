package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SubmittedAtConverter<T, I> extends AbstractBeanField<T, I> {

    // read from CSV conversion
    @Override
    protected Object convert(String value)  {
        return value;
    }

    // write to CSV conversion
    @Override
    protected String convertToWrite(Object value) {
        OffsetDateTime date = (OffsetDateTime) value;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return date.format(formatter);
    }
}
