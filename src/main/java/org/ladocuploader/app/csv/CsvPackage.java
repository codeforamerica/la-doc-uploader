package org.ladocuploader.app.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.ladocuploader.app.csv.CsvService.CsvType;

import static org.ladocuploader.app.csv.CsvService.CsvType.PARENT_GUARDIAN;
import static org.ladocuploader.app.csv.CsvService.CsvType.STUDENT;
import static org.ladocuploader.app.csv.CsvService.CsvType.RELATIONSHIP;
import static org.ladocuploader.app.csv.CsvService.CsvType.ECE_APPLICATION;
import static org.ladocuploader.app.csv.CsvService.CsvType.WIC_APPLICATION;

@Getter
public class CsvPackage {
  public enum CsvPackageType {
    ECE_PACKAGE (List.of(PARENT_GUARDIAN, STUDENT, RELATIONSHIP, ECE_APPLICATION)),
    WIC_PACKAGE (List.of(WIC_APPLICATION));

    private final List<CsvType> csvTypeList;
    CsvPackageType(List<CsvType> csvTypeList) {
      this.csvTypeList = csvTypeList;
    }

    public List<CsvType> getCsvTypeList() {
      return this.csvTypeList;
    }
  }

  @Setter
  private CsvPackageType packageType;

  private List<CsvDocument> csvDocumentList;

  private Map<CsvType, Map<UUID, String>> submissionErrorMap;

  private int totalSubmissionsProcessed;

  public CsvPackage(CsvPackageType packageType) {
    submissionErrorMap = new HashMap<>();
    csvDocumentList = new ArrayList<>();
    this.packageType = packageType;
  }

  public void addError(CsvType type, UUID submissionId, String errorMsg) {
    if (submissionId != null && errorMsg !=null) {
      submissionErrorMap.put(submissionId, errorMsg);
    }
  }

  public void addCsvDocument(CsvDocument csv) {
    if (csv == null) {
      return;
    }

    Optional<CsvDocument> maybeCsv = csvDocumentList.stream()
        .filter(d -> d.getCsvType().equals(csv.getCsvType()))
        .findFirst();

    // is it there already? if so, remove it and add the new one
    maybeCsv.ifPresent(csvDocument -> {
      csvDocumentList.remove(csvDocument);
      csvDocumentList.add(csv);
    });
  }

  public CsvDocument getCsvDocument(CsvType csvType) {
    Optional<CsvDocument> maybeCsv = csvDocumentList.stream()
        .filter(d -> d.getCsvType().equals(csvType))
        .findFirst();
    return maybeCsv.orElse(null);
  }
}

