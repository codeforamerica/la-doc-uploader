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
        if (dateMap.get("year") == null || dateMap.get("month") == null || dateMap.get("day") == null) {
            return "";
        }
        return String.format("%04d-%02d-%02d",
            dateMap.get("year"),
            dateMap.get("month"),
            dateMap.get("day")
        );
    }
}
