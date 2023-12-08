package org.ladocuploader.app.csv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.ladocuploader.app.csv.enums.CsvType;

@Getter
public class CsvDocument {

  @Setter
  private byte[] csvData;

  @Setter
  private CsvType csvType;

  // TODO Map to ERROR ENUM
  private final Map<UUID, String> errorMessages;

  public CsvDocument(CsvType csvType) {
     this(csvType, null);
  }

  public CsvDocument(CsvType csvType, byte [] csvData) {
      errorMessages = new HashMap<>();
      this.csvData = csvData;
      this.csvType = csvType;
  }

  public void addErrorMessage(UUID submissionId, String errorMsg) {
    errorMessages.put(submissionId, errorMsg);
  }
}
