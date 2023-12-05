package org.ladocuploader.app.csv.enums;

import static org.ladocuploader.app.csv.enums.CsvType.ECE_APPLICATION;
import static org.ladocuploader.app.csv.enums.CsvType.PARENT_GUARDIAN;
import static org.ladocuploader.app.csv.enums.CsvType.RELATIONSHIP;
import static org.ladocuploader.app.csv.enums.CsvType.STUDENT;
import static org.ladocuploader.app.csv.enums.CsvType.WIC_APPLICATION;

import java.util.List;
import org.ladocuploader.app.csv.CsvService;

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