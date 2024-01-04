package org.ladocuploader.app.data.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum PersonalSituationType {
  HOMELESS_SWITCH("experiencing homelessness", "personal-situations.which.option1"),
  DRUGS_ALCOHOL("struggling with drugs or alcohol", "personal-situations.which.option2"),
  DOMESTIC_ABUSE("experiencing domestic abuse", "personal-situations.which.option3"),
  DISABILITY("personal issue or disability", "personal-situations.which.option4"),
  OTHER("other", "personal-situations.which.option5");

  private final String value;
  private final String labelSrc;

  static private final Map<String, PersonalSituationType> MAP_BY_VALUE = new HashMap<>();

  static {
    for (PersonalSituationType type : PersonalSituationType.values()) {
      MAP_BY_VALUE.put(type.value, type);
    }
  }

  PersonalSituationType(String value, String labelSrc) {
    this.value = value;
    this.labelSrc = labelSrc;
  }
}