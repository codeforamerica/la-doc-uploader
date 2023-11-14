package org.ladocuploader.app.csv;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class CsvService {

    private final CsvGenerator csvGenerator;

    public CsvService(CsvGenerator csvGenerator) {
        this.csvGenerator = csvGenerator;
    }

    public byte[] generateParentGuardian(Submission submission) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        return csvGenerator.generateParentGuardian(submission);
    }

    /**
     * Generates a generic pdf file name from the flow and submission id that are part of the Submission.
     *
     * @param submission Submission to create the PDF filename for
     * @return a generic filename string, including the '.pdf' extension
     */
    public String generateCsvName(Submission submission) {
        return String.format("%s_%s.csv", submission.getFlow(), submission.getId().toString());
    }
}
