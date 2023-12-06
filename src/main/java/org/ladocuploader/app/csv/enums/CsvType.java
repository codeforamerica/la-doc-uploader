package org.ladocuploader.app.csv.enums;

import lombok.Getter;

@Getter
public enum CsvType {
  PARENT_GUARDIAN("ParentGuardian", "parent_guardian.csv"),
  STUDENT("Student", "student.csv"),
  RELATIONSHIP("Relationship", "relationship.csv"),
  ECE_APPLICATION("ECE Application", "application.csv"),
  WIC_APPLICATION("WIC Application", "application.csv");

  private final String name;
  private final String fileName;
  CsvType(String name, String fileName) {
    this.name = name;
    this.fileName = fileName;
  }
}

