package org.ladocuploader.app.csv.enums;

import lombok.Getter;

import static org.ladocuploader.app.csv.enums.CsvType.ECE_APPLICATION;
import static org.ladocuploader.app.csv.enums.CsvType.PARENT_GUARDIAN;
import static org.ladocuploader.app.csv.enums.CsvType.RELATIONSHIP;
import static org.ladocuploader.app.csv.enums.CsvType.STUDENT;
import static org.ladocuploader.app.csv.enums.CsvType.WIC_APPLICATION;

import java.util.List;

public enum CsvPackageType {
  ECE_PACKAGE (
          List.of(PARENT_GUARDIAN, STUDENT, RELATIONSHIP, ECE_APPLICATION),
          "nola-ps",
          false,
          true,
          true
  ),
  WIC_PACKAGE (
          List.of(WIC_APPLICATION),
          "dcfs",
          false,
          true,
          false
  );

  @Getter
  private final List<CsvType> csvTypeList;

  @Getter
  private final String uploadLocation;

  @Getter
  private final Boolean includeDocumentation;

  @Getter
  private final Boolean encryptPackage;

  @Getter
  private final Boolean createZipArchive;

  CsvPackageType(List<CsvType> csvTypeList, String uploadLocation, Boolean includeDocumentation, Boolean encryptPackage,
                 Boolean createZipArchive) {
    this.csvTypeList = csvTypeList;
    this.uploadLocation = uploadLocation;
    this.includeDocumentation = includeDocumentation;
    this.encryptPackage = encryptPackage;
    this.createZipArchive = createZipArchive;
  }

}