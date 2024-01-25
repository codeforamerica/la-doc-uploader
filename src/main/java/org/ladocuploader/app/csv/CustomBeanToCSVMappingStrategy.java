package org.ladocuploader.app.csv;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.StringUtils;

public class CustomBeanToCSVMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {

    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {

        String[] headersAsPerFieldName = getFieldMap().generateHeader(bean); // header name based on field name

        String[] header = new String[headersAsPerFieldName.length];

        for (int i = 0; i <= headersAsPerFieldName.length - 1; i++) {

            BeanField beanField = findField(i);

            String columnHeaderName = extractHeaderName(beanField); // header name based on @CsvBindByName annotation

            if (columnHeaderName.isEmpty()) // No @CsvBindByName or @CsvCustomBindByName is present
                columnHeaderName = headersAsPerFieldName[i]; // defaults to header name based on field name

            header[i] = columnHeaderName;
        }

        headerIndex.initializeHeaderIndex(header);

        return header;
    }

    private String extractHeaderName(final BeanField beanField) {
        var declaredAnnotations = beanField.getField().getDeclaredAnnotations();
        boolean fieldCustomBindByName = beanField.getField().getDeclaredAnnotationsByType(CsvCustomBindByName.class).length > 0;
        if (beanField == null || beanField.getField() == null || ((beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class).length == 0) & (!fieldCustomBindByName))) {
            return StringUtils.EMPTY;
        }

        if (!fieldCustomBindByName){
            final CsvBindByName bindByNameAnnotation = beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class)[0];
            return bindByNameAnnotation.column();
        } else {
            final CsvCustomBindByName customBindByNameAnnotation = beanField.getField().getDeclaredAnnotationsByType(CsvCustomBindByName.class)[0];
            return customBindByNameAnnotation.column();
//            return customBindByNameAnnotation.column();
        }


    }
}
