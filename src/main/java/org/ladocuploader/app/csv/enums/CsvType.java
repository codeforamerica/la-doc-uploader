package org.ladocuploader.app.csv.enums;

import lombok.Getter;

@Getter
public enum CsvType {
  PARENT_GUARDIAN("ParentGuardian"),
  STUDENT("Student"),
  RELATIONSHIP("Relationship"),
  ECE_APPLICATION("ECE Application"),
  WIC_APPLICATION("WIC Application");

  private final String name;
  CsvType(String name) {
    this.name = name;
  }
}

