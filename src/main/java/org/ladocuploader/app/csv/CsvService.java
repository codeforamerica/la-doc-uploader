package org.ladocuploader.app.csv;

import formflow.library.data.Submission;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import com.opencsv.CSVWriter;

@Service
public class CsvService {

    private final CsvGenerator csvGenerator;

    public CsvService(CsvGenerator csvGenerator) {
        this.csvGenerator = csvGenerator;
    }

//    public byte[] generateCsv(Submission submission, String flow) throws IOException {
//        return csvGenerator.generate(submission, flow);
//    }

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
