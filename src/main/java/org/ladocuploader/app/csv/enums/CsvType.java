package org.ladocuploader.app.csv.enums;

import lombok.Getter;

@Getter
public enum CsvType {
  PARENT_GUARDIAN("ParentGuardian", "parent_guardian"),
  STUDENT("Student", "student"),
  RELATIONSHIP("Relationship", "relationship"),
  ECE_APPLICATION("ECE Application", "application"),
  JEFFERSON_ECE("Jefferson ECE", "jefferson_ece"),
  WIC_APPLICATION("WIC Application", "application");

  private final String name;
  private final String fileNamePrefix;
  CsvType(String name, String fileNamePrefix) {
    this.name = name;
    this.fileNamePrefix = fileNamePrefix;
  }
}

