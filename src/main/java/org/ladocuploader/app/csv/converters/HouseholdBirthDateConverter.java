package org.ladocuploader.app.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HouseholdBirthDateConverter<T, I> extends AbstractBeanField<T, I> {

    @Override
    protected Object convert(String value)  {
        return value;
    }

    @Override
    protected String convertToWrite(Object value) {
        Map<String, Integer> dateMap = (Map) value;

        String date = String.format("%02d-%02d-%04d", dateMap.get("month"), dateMap.get("day"), dateMap.get("year"));

        return value.toString();
    }

}
