package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ComputedFieldConverter<T, I> extends AbstractBeanField<T, I> {

    @Override
    protected Object convert(String value)  {
        return value + "_CONVERTED";
    }

    @Override
    protected String convertToWrite(Object value) {

        return value + " converted to write";
    }

}
