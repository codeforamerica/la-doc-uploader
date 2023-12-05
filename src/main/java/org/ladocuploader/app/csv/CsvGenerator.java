package org.ladocuploader.app.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.csv.enums.CsvType;
import org.ladocuploader.app.csv.model.BaseCsvModel;
import org.ladocuploader.app.csv.model.ParentGuardianCsvModel;
import org.ladocuploader.app.csv.model.StudentCsvModel;
import org.ladocuploader.app.csv.model.RelationshipCsvModel;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

@Component
@Slf4j
public class CsvGenerator {
    // TODO: keep in mind for subflow unpacking etc.
//    https://stackoverflow.com/questions/77230117/custom-converter-for-opencsv

    public CsvDocument generateRelationshipCsvData(List<Submission> submissionList)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        List<BaseCsvModel> relationships = new ArrayList<>();

        for(Submission submission : submissionList) {
            relationships.addAll(RelationshipCsvModel.generateModel(submission));
        }

        byte[] data = generateCsv(RelationshipCsvModel.class, relationships);
        return new CsvDocument(CsvType.RELATIONSHIP, data);
    }

    public CsvDocument generateParentGuardianCsvData(List<Submission> submissionList)
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        List<BaseCsvModel> pgList = new ArrayList<>();

        for (Submission submission : submissionList) {
            BaseCsvModel pg = ParentGuardianCsvModel.generateModel(submission);
            pgList.add(pg);
        }

        byte[] data = generateCsv(ParentGuardianCsvModel.class, pgList);
        return new CsvDocument(CsvType.PARENT_GUARDIAN, data);
    }

    public CsvDocument generateStudentCsvData(List<Submission> submissionList)
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        List<BaseCsvModel> studentList = new ArrayList<>();

        for (Submission submission : submissionList) {
            List<BaseCsvModel> students = StudentCsvModel.generateModel(submission);
            studentList.addAll(students);
        }
        byte [] data = generateCsv(StudentCsvModel.class, studentList);
        return new CsvDocument(CsvType.STUDENT, data);
    }

    private byte[] generateCsv(Class classType, List<BaseCsvModel> objects) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        HeaderColumnMappingStrategy<BaseCsvModel> strategy = new HeaderColumnMappingStrategy<>();
        strategy.setType(classType);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
        CSVWriter writer = new CSVWriter(streamWriter);
        StatefulBeanToCsv<BaseCsvModel> beanToCsv = new StatefulBeanToCsvBuilder<BaseCsvModel>(writer)
                .withSeparator(',')
                .withMappingStrategy(strategy)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        beanToCsv.write(objects);
        streamWriter.flush();
        return stream.toByteArray();
    }
}
