package org.ladocuploader.app.csv;


import static org.ladocuploader.app.csv.enums.CsvType.JEFFERSON_ECE;
import static org.ladocuploader.app.csv.enums.CsvType.PARENT_GUARDIAN;
import static org.ladocuploader.app.csv.enums.CsvType.RELATIONSHIP;
import static org.ladocuploader.app.csv.enums.CsvType.STUDENT;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;

import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.csv.enums.CsvPackageType;
import org.ladocuploader.app.csv.enums.CsvType;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Slf4j
@Service
public class CsvService {

    private final CsvGenerator csvGenerator;

    /**
     *  Enum type representing the different CSV files that can be generated.
     */
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
    public CsvDocument generateCsvFormattedData(List<Submission> submissionList, CsvType csvType)
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
      return switch (csvType) {
        case STUDENT -> csvGenerator.generateStudentCsvData(submissionList);
        case RELATIONSHIP -> csvGenerator.generateRelationshipCsvData(submissionList);
        case PARENT_GUARDIAN -> csvGenerator.generateParentGuardianCsvData(submissionList);
        case ECE_APPLICATION -> csvGenerator.generateECEApplicationCsvData(submissionList);
        case WIC_APPLICATION -> csvGenerator.generateWICApplicationCsvData(submissionList);
        case JEFFERSON_ECE -> csvGenerator.generateJeffersionApplicationCsvData(submissionList);
        default -> {
          log.warn("Unknown CSV Type requested: {}", csvType.name());
          yield null;
        }
      };
    }

    /**
     * Returns a
     * @param submissionList
     * @param packageType
     * @return
     */
    public CsvPackage generateCsvPackage(List<Submission> submissionList, CsvPackageType packageType)
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        CsvPackage csvPackage = new CsvPackage(packageType);

        List<CsvType> csvTypes = packageType.getCsvTypeList();

        for (CsvType csvType : csvTypes) {
            csvPackage.addCsvDocument(generateCsvFormattedData(submissionList, csvType));
        }

        return csvPackage;
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
