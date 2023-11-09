package org.ladocuploader.app.csv;

import com.opencsv.CSVWriter;
import formflow.library.data.Submission;
import formflow.library.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CsvGenerator {

    private final SubmissionFieldPreparers submissionFieldPreparers;
    private final PdfMapConfiguration pdfMapConfiguration;
    private final PdfFieldMapper pdfFieldMapper;
//    private final PDFBoxFieldFiller pdfBoxFieldFiller;

    public CsvGenerator(SubmissionFieldPreparers submissionFieldPreparers,
                        PdfFieldMapper pdfFieldMapper, PdfMapConfiguration pdfMapConfiguration) {
        this.submissionFieldPreparers = submissionFieldPreparers;
        this.pdfMapConfiguration = pdfMapConfiguration;
        this.pdfFieldMapper = pdfFieldMapper;
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
