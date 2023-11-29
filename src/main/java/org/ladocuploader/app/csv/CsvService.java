package org.ladocuploader.app.csv;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Slf4j
@Service
public class CsvService {

    private final CsvGenerator csvGenerator;

    /**
     *  Enum type representing the different CSV files that can be generated.
     */
    @Getter
    public enum CsvType {
        ParentGuardian,// ("ParentGuardian"),
        Student, // ("Student"),
        Relationship, // ("Relationship"),
        Application;// ("Application");
/*
        private String name;
        CsvType(String name) {
            this.name = name;
        }*/
    }

    public CsvService(CsvGenerator csvGenerator) {
        this.csvGenerator = csvGenerator;
    }

    /**
     * A method that generates the specific CsvType CSV data for the passed in list of Submissions.
     *
     * @param submissionList List of submission to include in the CSV data
     * @param csvType The type of CSV to generate the data for
     * @return a byte array of CSV formatted data
     * @throws CsvRequiredFieldEmptyException
     * @throws CsvDataTypeMismatchException
     * @throws IOException
     */
    public byte[] generateCsvFormattedData(List<Submission> submissionList, CsvType csvType)
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        switch (csvType) {
            case Student: return csvGenerator.generateStudentCsvData(submissionList);
            case Relationship: return csvGenerator.generateRelationshipCsvData(submissionList);
            case ParentGuardian: return csvGenerator.generateParentGuardianCsvData(submissionList);
            default:
                log.warn("Unknown CSV Type requested: {}", csvType.name());
                return null;
        }
    }

    /**
     * Generates a generic pdf file name from the flow name and CSV Type, along with a timestamp of milliseconds since Epoch.
     *
     * @param flowName - name of the flow
     * @param csvType - the type of the CVS we are generating
     * @return A filename to use for the CSV
     */
    public String generateCsvName(String flowName, CsvType csvType) {
        return String.format("%s_%s_%s.csv", flowName, csvType.name(), Instant.now().toEpochMilli());
    }
}
