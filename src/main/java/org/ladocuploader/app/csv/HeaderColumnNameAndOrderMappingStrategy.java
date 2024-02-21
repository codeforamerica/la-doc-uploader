package org.ladocuploader.app.csv;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.comparator.LiteralComparator;
import com.opencsv.exceptions.CsvBadConverterException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

public class HeaderColumnNameAndOrderMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        // overriding this method to allow us to preserve the header column name casing
        super.generateHeader(bean);
        String[] orderedHeaders = type.getAnnotation(CsvBindByNameOrder.class).value();
        return orderedHeaders;
    }


    @Override
    protected void loadFieldMap() throws CsvBadConverterException {
        // overriding this method to support setting column order by the custom `CsvBindByNameOrder` annotation
        if (writeOrder == null && type.isAnnotationPresent(CsvBindByNameOrder.class)) {
            setColumnOrderOnWrite(
                    new LiteralComparator<>(Arrays.stream(type.getAnnotation(CsvBindByNameOrder.class).value())
                            .map(String::toUpperCase).toArray(String[]::new)));
        }
        super.loadFieldMap();
    }

    private String extractHeaderName(final BeanField beanField) {
        if (beanField == null || beanField.getField() == null
                || beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class).length == 0) {
            return StringUtils.EMPTY;
        }

        if (beanField.getField().isAnnotationPresent(CsvBindByName.class)) {
            return beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class)[0].column();
        } else if (beanField.getField().isAnnotationPresent(CsvCustomBindByName.class)) {
            return beanField.getField().getDeclaredAnnotationsByType(CsvCustomBindByName.class)[0].column();
        }
        return StringUtils.EMPTY;

    }
}
