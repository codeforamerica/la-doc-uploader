package org.ladocuploader.app.csv.enums;

import lombok.Getter;

import static org.ladocuploader.app.csv.enums.CsvType.ECE_APPLICATION;
import static org.ladocuploader.app.csv.enums.CsvType.PARENT_GUARDIAN;
import static org.ladocuploader.app.csv.enums.CsvType.RELATIONSHIP;
import static org.ladocuploader.app.csv.enums.CsvType.STUDENT;
import static org.ladocuploader.app.csv.enums.CsvType.WIC_APPLICATION;

import java.util.List;

public enum CsvPackageType {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
  ECE_PACKAGE (
          List.of(PARENT_GUARDIAN, STUDENT, RELATIONSHIP, ECE_APPLICATION),
          "nola-ps",
          true,
          true
  ),
  WIC_PACKAGE (
          List.of(WIC_APPLICATION),
          "dcfs",
          false,
          true
  );

  @Getter
=======
  ECE_PACKAGE (List.of(PARENT_GUARDIAN, STUDENT, RELATIONSHIP, ECE_APPLICATION), "/la-du-moveit-transfer/nola-ps"),
  WIC_PACKAGE (List.of(WIC_APPLICATION), "/la-du-moveit-transfer/dcfs");
=======
  ECE_PACKAGE (List.of(PARENT_GUARDIAN, STUDENT, RELATIONSHIP, ECE_APPLICATION), "/la-du-moveit-transfer/nola-ps", true),
  WIC_PACKAGE (List.of(WIC_APPLICATION), "/la-du-moveit-transfer/dcfs", false);
>>>>>>> 33f952b0 (do not include documents for WIC - ECE only)

>>>>>>> 449ac468 (add base class + 2 subclasses extending PGPEncryptor)
=======
  ECE_PACKAGE (
          List.of(PARENT_GUARDIAN, STUDENT, RELATIONSHIP, ECE_APPLICATION),
          "/la-du-moveit-transfer/nola-ps",
          true,
          false
  ),
  WIC_PACKAGE (
          List.of(WIC_APPLICATION),
          "/la-du-moveit-transfer/dcfs",
          false,
          true
  );

  @Getter
>>>>>>> b663d9a1 (add override for in-memory upload file in sftp impl, encrypt for packages with encryption flag)
  private final List<CsvType> csvTypeList;

  @Getter
  private final String uploadLocation;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> b663d9a1 (add override for in-memory upload file in sftp impl, encrypt for packages with encryption flag)
  @Getter
  private final Boolean includeDocumentation;

  @Getter
  private final Boolean encryptPackage;

  CsvPackageType(List<CsvType> csvTypeList, String uploadLocation, Boolean includeDocumentation, Boolean encryptPackage) {
<<<<<<< HEAD
    this.csvTypeList = csvTypeList;
    this.uploadLocation = uploadLocation;
    this.includeDocumentation = includeDocumentation;
    this.encryptPackage = encryptPackage;
  }

=======
  private final Boolean includeDocuments;
  CsvPackageType(List<CsvType> csvTypeList, String uploadLocation, Boolean includeDocuments) {
=======
>>>>>>> b663d9a1 (add override for in-memory upload file in sftp impl, encrypt for packages with encryption flag)
    this.csvTypeList = csvTypeList;
    this.uploadLocation = uploadLocation;
    this.includeDocumentation = includeDocumentation;
    this.encryptPackage = encryptPackage;
  }


<<<<<<< HEAD
  public String getUploadLocation() {
    return this.uploadLocation;
  }

  public Boolean getIncludeDocuments() {return this.includeDocuments;}
>>>>>>> 33f952b0 (do not include documents for WIC - ECE only)
=======
>>>>>>> b663d9a1 (add override for in-memory upload file in sftp impl, encrypt for packages with encryption flag)
}