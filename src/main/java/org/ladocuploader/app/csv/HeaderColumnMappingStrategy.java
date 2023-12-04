package org.ladocuploader.app.csv;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.util.Arrays;


/**
 * A class that helps generate the header in lower case characters
 * versus upper case (default).
 * @param <T>
 */
public class HeaderColumnMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {
    public HeaderColumnMappingStrategy(Class<? extends T> clazz) {
        setType(clazz);
    }

    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        return Arrays.stream(super.generateHeader(bean))
            .map(String::toLowerCase).toArray(String[]::new);
    }
}
