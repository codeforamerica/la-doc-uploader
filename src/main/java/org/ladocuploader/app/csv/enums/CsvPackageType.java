package org.ladocuploader.app.csv.enums;

import static org.ladocuploader.app.csv.enums.CsvType.ECE_APPLICATION;
import static org.ladocuploader.app.csv.enums.CsvType.PARENT_GUARDIAN;
import static org.ladocuploader.app.csv.enums.CsvType.RELATIONSHIP;
import static org.ladocuploader.app.csv.enums.CsvType.STUDENT;
import static org.ladocuploader.app.csv.enums.CsvType.WIC_APPLICATION;

import java.util.List;

public enum CsvPackageType {
  ECE_PACKAGE (List.of(PARENT_GUARDIAN, STUDENT, RELATIONSHIP, ECE_APPLICATION), "/la-du-moveit-transfer/nola-ps", true),
  WIC_PACKAGE (List.of(WIC_APPLICATION), "/la-du-moveit-transfer/dcfs", false);

  private final List<CsvType> csvTypeList;

  private final String uploadLocation;

  private final Boolean includeDocuments;
  CsvPackageType(List<CsvType> csvTypeList, String uploadLocation, Boolean includeDocuments) {
    this.csvTypeList = csvTypeList;
    this.uploadLocation = uploadLocation;
    this.includeDocuments = includeDocuments;
  }

  public List<CsvType> getCsvTypeList() {
    return this.csvTypeList;
  }

  public String getUploadLocation() {
    return this.uploadLocation;
  }

  public Boolean getIncludeDocuments() {return this.includeDocuments;}
}