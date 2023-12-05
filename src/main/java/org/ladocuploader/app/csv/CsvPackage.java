package org.ladocuploader.app.csv;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.ladocuploader.app.csv.enums.CsvType;
import org.ladocuploader.app.csv.enums.CsvPackageType;

@Getter
public class CsvPackage {

  @Setter
  private CsvPackageType packageType;

  private Map<CsvType, CsvDocument> csvDocumentMap;

  private int totalSubmissionsProcessed;

  public CsvPackage(CsvPackageType packageType) {
    csvDocumentMap = new HashMap<>();
    this.packageType = packageType;
  }

  public void addError(CsvType csvType, UUID submissionId, String errorMsg) {
    if (csvDocumentMap.containsKey(csvType)) {
      CsvDocument csv = csvDocumentMap.get(csvType);
      csv.addErrorMessage(submissionId, errorMsg);
    }
  }

  public Map<UUID, String> getErrorMessages(CsvType csvType) {
    if (csvDocumentMap.containsKey(csvType)) {
      return csvDocumentMap.get(csvType).getErrorMessages();
    }
    return null;
  }

  public Map<CsvType, Map<UUID, String>> getErrorMessages() {
    Map<CsvType, Map<UUID, String>> errorMap = new HashMap<>();
    csvDocumentMap.forEach((csvType, csv) -> {
      errorMap.put(csvType, csv.getErrorMessages());
    });
    return errorMap;
  }

  public void addCsvDocument(CsvDocument csv) {
    if (csv == null) {
      return;
    }
    csvDocumentMap.put(csv.getCsvType(), csv);
  }

  public CsvDocument getCsvDocument(CsvType csvType) {
    return csvDocumentMap.get(csvType);
  }
}

