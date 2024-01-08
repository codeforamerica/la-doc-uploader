package org.ladocuploader.app.data.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum RaceType {
  // Note: it is intentional that both American Indian and Alaskan Native map to "AN".  There
  // is only one choice on the form that encompasses both of them.
  AMERICAN_INDIAN("American Indian", "AN", "race-selection.option1"),
  ALASKAN_NATIVE("Alaskan Native", "AN", "race-selection.option2"),
  ASIAN("Asian", "AS", "race-selection.option3"),
  BLACK_OR_AFRICAN_AMERICA("Black or African American", "BL", "race-selection.option4"),
  NATIVE_HAWAIIAN_OR_OTHER_PACIFIC_ISLANDER("Native Hawaiian or Other Pacific Islander", "PI", "race-selection.option5"),
  WHITE("White", "WH", "race-selection.option6");

  private final String value;
  private final String abbreviation;
  private final String labelSrc;

  static private final Map<String, RaceType> MAP_BY_VALUE = new HashMap<>();

  static {
    for( RaceType type : RaceType.values()) {
      MAP_BY_VALUE.put(type.value, type);
    }
  }

  RaceType(String value, String abbreviation, String labelSrc) {
    this.value = value;
    this.abbreviation = abbreviation;
    this.labelSrc = labelSrc;
  }

  public static String getAbbreviationFromValue(String value) {
    RaceType raceType = (RaceType) MAP_BY_VALUE.get(value);
    return raceType != null ? raceType.abbreviation : null;
  }
}
