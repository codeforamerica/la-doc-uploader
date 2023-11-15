package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ComputedFieldConverter<T, I> extends AbstractBeanField<T, I> {

    @Override
    protected Object convert(String value)  {
        return value + "_CONVERTED";
    }

    @Override
    protected String convertToWrite(Object value) throws CsvDataTypeMismatchException {

        return "converted to write";
    }

}
