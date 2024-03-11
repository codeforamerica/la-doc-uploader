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
  @Getter
  private CsvPackageType packageType;

  private Map<CsvType, CsvDocument> csvDocumentMap;

  private int totalSubmissionsProcessed;

  public CsvPackage(CsvPackageType packageType) {
    csvDocumentMap = new HashMap<>();
    this.packageType = packageType;
  }

  public Map<UUID, String> getErrorMessages(CsvType csvType) {
    if (csvDocumentMap.containsKey(csvType)) {
      return csvDocumentMap.get(csvType).getErrorMessages();
    }
    return null;
  }

  public Map<UUID, Map<CsvType, String>> getErrorMessages() {
    Map<UUID, Map<CsvType, String>> errorMap = new HashMap<>();

    csvDocumentMap.forEach((csvType, csv) -> {
      Map<UUID, String> docErrors = csv.getErrorMessages();
      docErrors.entrySet()
          .forEach(entry -> {
            errorMap.put(entry.getKey(), Map.of(csvType, entry.getValue()));
      });
    });

    return errorMap;

/*
    Map<CsvType, Map<UUID, String>> errorMap = new HashMap<>();
    csvDocumentMap.forEach((csvType, csv) -> {
      errorMap.put(csvType, csv.getErrorMessages());
    });
    return errorMap;

 */
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

