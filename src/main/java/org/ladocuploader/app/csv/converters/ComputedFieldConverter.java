package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ComputedFieldConverter extends AbstractBeanField {
    @Override
    protected Object convert(String value)  {
        return value + "_CONVERTED";
    }
}
