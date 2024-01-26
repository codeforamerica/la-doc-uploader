package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class InSchoolOrTrainingConverter<T, I> extends AbstractBeanField<T, I> {

    @Override
    protected Object convert(String value)  {
        return value;
    }

    @Override
    protected String convertToWrite(Object value) {
        Map<String, Integer> dateMap = (Map) value;
        return "Yes";
    }
}
