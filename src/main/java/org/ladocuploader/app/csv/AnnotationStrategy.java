package org.ladocuploader.app.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

public class AnnotationStrategy<T> extends HeaderColumnNameTranslateMappingStrategy<T> {
    Map<String, String> columnMap = new HashMap<>();
    public AnnotationStrategy(Class<? extends T> clazz) {

        for (Field field : clazz.getDeclaredFields()) {
            CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
            if (annotation != null) {

                columnMap.put(field.getName().toUpperCase(), annotation.column());
            }
        }
        setType(clazz);
    }

    @Override
    public String getColumnName(int col) {
        String name = headerIndex.getByPosition(col);
        return name;
    }

    public String getColumnName1(int col) {
        String name = headerIndex.getByPosition(col);
        if(name != null) {
            name = columnMap.get(name);
        }
        return name;
    }
    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        String[] result = super.generateHeader(bean);
        for (int i = 0; i < result.length; i++) {
            result[i] = getColumnName1(i);
        }
        return result;
    }
}
