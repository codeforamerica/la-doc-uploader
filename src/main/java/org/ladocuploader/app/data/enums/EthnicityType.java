package org.ladocuploader.app.data.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum EthnicityType {
  HISPANIC_OR_LATINO("Hispanic or Latino", "Y", "ethnicity-selection.option1"),
  NOT_HISPANIC_OR_LATINO("Not Hispanic or Latino", "N", "ethnicity-selection.option2");

  private final String value;
  private final String abbreviation;
  private final String labelSrc;

  public static final Map<String, EthnicityType> MAP_BY_VALUE = new HashMap<>();
  static {
    for(EthnicityType type: EthnicityType.values()) {
      MAP_BY_VALUE.put(type.value, type);
    }
  }

  EthnicityType(String value, String abbreviation, String labelSrc) {
    this.value = value;
    this.abbreviation = abbreviation;
    this.labelSrc = labelSrc;
  }

  public static String getAbbreviationFromValue(String value) {
    EthnicityType ethnicityType = (EthnicityType) MAP_BY_VALUE.get(value);

    return ethnicityType != null ? ethnicityType.abbreviation : null;
  }
}
