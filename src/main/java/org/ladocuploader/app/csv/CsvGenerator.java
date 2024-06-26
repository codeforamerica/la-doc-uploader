package org.ladocuploader.app.csv;

import com.opencsv.CSVWriter;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.csv.enums.CsvType;
import org.ladocuploader.app.csv.model.BaseCsvModel;
import org.ladocuploader.app.csv.model.ECEApplicationCsvModel;
import org.ladocuploader.app.csv.model.JeffersonCsvModel;
import org.ladocuploader.app.csv.model.ParentGuardianCsvModel;
import org.ladocuploader.app.csv.model.StudentCsvModel;
import org.ladocuploader.app.csv.model.RelationshipCsvModel;
import org.ladocuploader.app.csv.model.WICApplicationCsvModel;
import org.springframework.stereotype.Component;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

@Component
@Slf4j
public class CsvGenerator {


    private static final String ECE_ELIGIBLE = "interestedInEceInd";

    private boolean isEceEligibleAndInterested(Submission submission) {
        if (submission == null) {
            return false;
        }
        String eceInterest = (String) submission.getInputData().getOrDefault("interestedInEceInd", "false");
        return "true".equals(eceInterest);
    }
     
    private boolean isWicEligibleAndInterested(Submission submission) {
        if (submission == null) {
            return false;
        }
        String wicInterest = (String) submission.getInputData().getOrDefault("interestedInWicInd", "false");
        return "true".equals(wicInterest);
    }

    public CsvDocument generateRelationshipCsvData(List<Submission> submissionList)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        List<BaseCsvModel> relationships = new ArrayList<>();

        for(Submission submission : submissionList) {
            if(isEceEligibleAndInterested(submission)) {
                relationships.addAll(RelationshipCsvModel.generateModel(submission));
            }
        }

        return generateCsv(CsvType.RELATIONSHIP, RelationshipCsvModel.class, relationships);
    }


    public CsvDocument generateParentGuardianCsvData(List<Submission> submissionList)
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        List<BaseCsvModel> pgList = new ArrayList<>();

        for (Submission submission : submissionList) {
            if(isEceEligibleAndInterested(submission)) {
                BaseCsvModel pg = ParentGuardianCsvModel.generateModel(submission);
                pgList.add(pg);
            }
        }

        return generateCsv(CsvType.PARENT_GUARDIAN,ParentGuardianCsvModel.class, pgList);
    }

    public CsvDocument generateStudentCsvData(List<Submission> submissionList)
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        List<BaseCsvModel> studentList = new ArrayList<>();

        for (Submission submission : submissionList) {
            if(isEceEligibleAndInterested(submission)) {
                List<BaseCsvModel> students = StudentCsvModel.generateModel(submission);
                studentList.addAll(students);
            }
        }
        return generateCsv(CsvType.STUDENT, StudentCsvModel.class, studentList);
    }

    public CsvDocument generateECEApplicationCsvData(List<Submission> submissionList)
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        List<BaseCsvModel> applicationList = new ArrayList<>();

        for (Submission submission : submissionList) {
            if(isEceEligibleAndInterested(submission)) {
                BaseCsvModel application = ECEApplicationCsvModel.generateModel(submission);
                applicationList.add(application);
            }
        }
        return generateCsv(CsvType.ECE_APPLICATION, ECEApplicationCsvModel.class, applicationList);
    }

    public CsvDocument generateWICApplicationCsvData(List<Submission> submissionList)
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        List<BaseCsvModel> applicationList = new ArrayList<>();

        for (Submission submission : submissionList) {
            if(isWicEligibleAndInterested(submission)) {
                BaseCsvModel application = WICApplicationCsvModel.generateModel(submission);
                applicationList.add(application);
            }
        }
        return generateCsv(CsvType.WIC_APPLICATION, WICApplicationCsvModel.class, applicationList);
    }
    
    public CsvDocument generateJeffersionApplicationCsvData(List<Submission> submissionList)
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {

        List<BaseCsvModel> applicationList = new ArrayList<>();

        for (Submission submission : submissionList) {
            if(isEceEligibleAndInterested(submission)) {
                BaseCsvModel application = JeffersonCsvModel.generateModel(submission);
                applicationList.add(application);
            }
        }
        return generateCsv(CsvType.JEFFERSON_ECE, JeffersonCsvModel.class, applicationList);
    }

    private CsvDocument generateCsv(CsvType csvType, Class classType, List<BaseCsvModel> objects) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        CsvDocument csv = new CsvDocument(csvType);
        HeaderColumnNameAndOrderMappingStrategy<BaseCsvModel> mappingStrategy = new HeaderColumnNameAndOrderMappingStrategy<>();
        mappingStrategy.setType(classType);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
        CSVWriter writer = new CSVWriter(streamWriter);
        StatefulBeanToCsv<BaseCsvModel> beanToCsv = new StatefulBeanToCsvBuilder<BaseCsvModel>(writer)
                .withSeparator(',')
                .withMappingStrategy(mappingStrategy)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        // write one line at a time to gather exceptions. Otherwise, we just have access to CsvExceptions and
        // that's not particularly useful.
        for (BaseCsvModel item : objects) {
            try {
                beanToCsv.write(item);
            } catch (Exception e) {
                log.warn("Exception caught while processing CSV (submission id: '{}', type: '{}'): '{}'",
                    item.getSubmissionId(), csvType, e.getMessage());
                if (e instanceof CsvException) {
                    // NOTE: this only captures one error per type per submission.
                    csv.addErrorMessage(item.getSubmissionId(), e.getMessage());
                }
            }
        }
        streamWriter.flush();
        csv.setCsvData(stream.toByteArray());
        return csv;
    }
}
