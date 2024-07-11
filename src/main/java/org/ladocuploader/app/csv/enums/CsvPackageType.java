package org.ladocuploader.app.csv.enums;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.ladocuploader.app.cli.PGPEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.ladocuploader.app.csv.enums.CsvType.*;

public enum CsvPackageType {

  ECE_ORLEANS_PACKAGE (
          List.of(PARENT_GUARDIAN, STUDENT, RELATIONSHIP, ECE_APPLICATION),
          "nola-ps",
          false,
          true,
          true
          ),

  ECE_JEFFERSON_PACKAGE (
          List.of(PARENT_GUARDIAN, STUDENT, RELATIONSHIP, ECE_APPLICATION),
          "jefferson-apps",
          false,
          true,
          true),
  WIC_PACKAGE (
          List.of(WIC_APPLICATION),
          "dcfs",
          false,
          true,
          false);

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
  @Getter
  private PGPEncryptor pgpEncryptor;

  @Component
  public static class MyEnumInjector {

    private final PGPEncryptor wicPgpEncryptor;
    private final PGPEncryptor eceOrleansPgpEncryptor;

    private final PGPEncryptor eceJeffersonPgpEncryptor;
    @Autowired
    public MyEnumInjector(
            PGPEncryptor wicPgpEncryptor,
            PGPEncryptor eceOrleansPgpEncryptor,
            PGPEncryptor eceJeffersonPgpEncryptor) {

      this.wicPgpEncryptor = wicPgpEncryptor;
      this.eceOrleansPgpEncryptor = eceOrleansPgpEncryptor;
      this.eceJeffersonPgpEncryptor = eceJeffersonPgpEncryptor;
    }

    @PostConstruct
    public void postConstruct() {
      CsvPackageType.WIC_PACKAGE.setEncryptionService(wicPgpEncryptor);
      CsvPackageType.ECE_JEFFERSON_PACKAGE.setEncryptionService(eceJeffersonPgpEncryptor);
      CsvPackageType.ECE_ORLEANS_PACKAGE.setEncryptionService(eceOrleansPgpEncryptor);
    }
  }

  private void setEncryptionService(PGPEncryptor pgpEncryptor) {
    this.pgpEncryptor = pgpEncryptor;
  }

  CsvPackageType(List<CsvType> csvTypeList, String uploadLocation, Boolean includeDocumentation, Boolean encryptPackage,
                 Boolean createZipArchive) {
    this.csvTypeList = csvTypeList;
    this.uploadLocation = uploadLocation;
    this.includeDocumentation = includeDocumentation;
    this.encryptPackage = encryptPackage;
    this.createZipArchive = createZipArchive;
  }

}