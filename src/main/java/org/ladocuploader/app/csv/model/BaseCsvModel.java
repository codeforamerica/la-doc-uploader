package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvIgnore;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

public class BaseCsvModel {

    @Setter
    @Getter
    protected UUID submissionId;
    @CsvIgnore
    final static ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}