package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

public class BaseCsvModel {
    final static ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
         //   .allowIfSubType("org.ladocuploader.app.csv.model")
         //   .allowIfSubType("java.util.LinkedHashMap")
         //   .build();
        //mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
    }
}
