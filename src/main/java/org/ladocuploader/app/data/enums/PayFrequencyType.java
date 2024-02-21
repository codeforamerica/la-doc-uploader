package org.ladocuploader.app.data.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum PayFrequencyType {
  EVERY_WEEK("Every week", "job-pay-period.every-week"),
  EVERY_TWO_WEEKS("Every 2 weeks", "job-pay-period.every-two-weeks"),
  TWICE_A_MONTH("Twice a month", "job-pay-period.twice-a-month"),
  EVERY_MONTH("Every month", "job-pay-period.every-month"),
  VARIES("It varies", "job-pay-period.it-varies");

  private final String value;
  private final String labelSrc;

  static private final Map<String, PayFrequencyType> MAP_BY_VALUE = new HashMap<>();

  static {
    for (PayFrequencyType type : PayFrequencyType.values()) {
      MAP_BY_VALUE.put(type.value, type);
    }
  }

  PayFrequencyType(String value, String labelSrc) {
    this.value = value;
    this.labelSrc = labelSrc;
  }

  public static String getLabelSrcFromValue(String value) {
    PayFrequencyType expenseType = (PayFrequencyType) MAP_BY_VALUE.get(value);
    return expenseType != null ? expenseType.labelSrc : null;
  }
}