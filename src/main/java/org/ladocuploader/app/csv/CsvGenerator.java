package org.ladocuploader.app.csv;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvBindByName;
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
import java.util.stream.Collectors;

@Component
@Slf4j
public class CsvGenerator {
    // TODO: keep in mind for subflow unpacking etc.
//    https://stackoverflow.com/questions/77230117/custom-converter-for-opencsv

    public byte[] generateRelationship(Submission submission)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        List<BaseCsvModel> relationships = Relationship.generateModel(submission);

        return generateCsv(ParentGuardian.class, relationships);
    }

    public byte[] generateCsv(Class classType, List<BaseCsvModel> objects) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        AnnotationStrategy<BaseCsvModel> strategy = new AnnotationStrategy<>(classType);
        strategy.setType(classType);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
        CSVWriter writer = new CSVWriter(streamWriter);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(',')
                .withMappingStrategy(strategy)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        beanToCsv.write(objects);
        streamWriter.flush();
        return stream.toByteArray();
    }


    // TODO: adapt for list of submissions
    public byte[] generateParentGuardian(Submission submission)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        BaseCsvModel pg = ParentGuardian.generateModel(submission);

        List<BaseCsvModel> pgList = new ArrayList<>();
        pgList.add(pg);

        return generateCsv(ParentGuardian.class, pgList);
    }
}
