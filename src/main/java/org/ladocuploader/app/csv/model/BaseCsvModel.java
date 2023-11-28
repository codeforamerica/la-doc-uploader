package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseCsvModel {

    final static ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
