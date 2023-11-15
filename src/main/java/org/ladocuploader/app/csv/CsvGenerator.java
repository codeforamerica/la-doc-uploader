package org.ladocuploader.app.csv;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;
import formflow.library.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.csv.model.ParentGuardian;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CsvGenerator {


    // TODO: adapt for list of submissions
    public byte[] generateParentGuardian(Submission submission)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        Map<String, Object> inputData = submission.getInputData();
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
