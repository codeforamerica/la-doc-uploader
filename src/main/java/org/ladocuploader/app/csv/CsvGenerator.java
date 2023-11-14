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

//    private final PDFBoxFieldFiller pdfBoxFieldFiller;

    // TODO: modify to generate for any CSV model
    public CsvGenerator() {

    }

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

    /**
     * Generates a CSV based on Submission data and a certain Form Flow
     *
     * @param flow       the form flow we are working with
     * @param submission the submission we are going to map the data of
     * @return A CSVFile which contains the path to the newly created and filled in CSV file
     */
//    public byte[] generate(Submission submission, Object csvModel, String flow) {
//        StringWriter writer = new StringWriter();
//        CSVWriter csvWriter = new CSVWriter(writer);
//        List<Object> records
////        List<SubmissionField> submissionFields = submissionFieldPreparers.prepareSubmissionFields(submission);
////        List<PdfField> csvFields = pdfFieldMapper.map(submissionFields, flow);
////        Map<String, String> submissionMap = new HashMap<>();
////        submissionFields.forEach(f -> {
////            log.info(String.valueOf(f));
////            if ( (f) instanceof SingleField ){
////                submissionMap.put(f.getName(), ((SingleField) f).getValue());
////            }
////
////        });
////        StringBuilder builder = new StringBuilder();
////        builder.append("first_name, middle_name, last_name, email_address, phone_number, reference_id\n");
////
//////        builder.append(csvFields.get("first_name").value()).append(",");
////        builder.append(submissionMap.get("middle_name")).append(",");
////        builder.append(submissionMap.get("last_name")).append(",");
////        builder.append(submissionMap.get("email_address")).append(",");
////        builder.append(submissionMap.get("phone_number")).append(",");
////        builder.append(submissionMap.get("reference_id"));
//
//        return builder.toString().getBytes();
//    }
}
