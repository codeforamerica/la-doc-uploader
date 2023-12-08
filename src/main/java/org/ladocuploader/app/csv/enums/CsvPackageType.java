package org.ladocuploader.app.csv.enums;

import static org.ladocuploader.app.csv.enums.CsvType.ECE_APPLICATION;
import static org.ladocuploader.app.csv.enums.CsvType.PARENT_GUARDIAN;
import static org.ladocuploader.app.csv.enums.CsvType.RELATIONSHIP;
import static org.ladocuploader.app.csv.enums.CsvType.STUDENT;
import static org.ladocuploader.app.csv.enums.CsvType.WIC_APPLICATION;

import java.util.List;
import org.ladocuploader.app.csv.CsvService;

public enum CsvPackageType {

  // TODO: abstract environment
  ECE_PACKAGE (List.of(PARENT_GUARDIAN, STUDENT, RELATIONSHIP, ECE_APPLICATION), "/la-du-moveit-transfer/nola-ps-staging/"),
  WIC_PACKAGE (List.of(WIC_APPLICATION), "/la-du-moveit-transfer/wic-staging/");

  private final List<CsvType> csvTypeList;

  private final String uploadLocation;
  CsvPackageType(List<CsvType> csvTypeList, String uploadLocation) {
    this.csvTypeList = csvTypeList;
    this.uploadLocation = uploadLocation;
  }

  public List<CsvType> getCsvTypeList() {
    return this.csvTypeList;
  }

  public String getUploadLocation() {
    return this.uploadLocation;
  }
}