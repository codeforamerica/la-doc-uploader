package org.ladocuploader.app.csv;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.csv.model.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.*;

@Component
@Slf4j
public class CsvGenerator {
    // TODO: keep in mind for subflow unpacking etc.
//    https://stackoverflow.com/questions/77230117/custom-converter-for-opencsv

    public byte[] generateRelationship(Submission submission)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        Map<String, Object> inputData = submission.getInputData();
        UUID first_person_id = submission.getId();
        List<Relationship> relationships = new ArrayList<>();
        List<Map<String, Object>> households = (List<Map<String, Object>>) inputData.get("household");
        final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        for (Map<String, Object> household : households){
            household.put("first_person_id", first_person_id);
            relationships.add(mapper.convertValue(household, Relationship.class));
        }
        AnnotationStrategy<Relationship> strategy = new AnnotationStrategy<>();
        strategy.setType(Relationship.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
        CSVWriter writer = new CSVWriter(streamWriter);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(',')
                .withMappingStrategy(strategy)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        beanToCsv.write(relationships);
        streamWriter.flush();
        return stream.toByteArray();
    }


    // TODO: adapt for list of submissions
    public byte[] generateParentGuardian(Submission submission)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        Map<String, Object> inputData = submission.getInputData();
        inputData.put("id", submission.getId());
        List<ParentGuardian> pgList = new ArrayList<>();
        final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final ParentGuardian pg = mapper.convertValue(inputData, ParentGuardian.class);
        pgList.add(pg);
        AnnotationStrategy<ParentGuardian> strategy = new AnnotationStrategy<>();
        strategy.setType(pg.getClass());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
        CSVWriter writer = new CSVWriter(streamWriter);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(',')
                .withMappingStrategy(strategy)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        beanToCsv.write(pgList);
        streamWriter.flush();
        return stream.toByteArray();
    }
}
