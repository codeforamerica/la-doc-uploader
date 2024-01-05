package org.ladocuploader.app.data.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum UtilitiesExpenseType {
  HEATING("heating", "utilities.heating"),
  COOLING("cooling", "utilities.cooling"),
  ELECTRICITY("electricity", "utilities.electricity"),
  WATER("water", "utilities.water"),
  PHONE("phone", "utilities.phone"),
  GARBAGE("garbage", "utilities.garbage"),
  SEWER("sewer", "utilities.sewer"),
  COOKING_FUEL("cookingFuel", "utilities.cooking-fuel"),
  OTHER("otherUtilitiesExpenses", "utilities.other");

  private final String value;
  private final String labelSrc;

  static private final Map<String, UtilitiesExpenseType> MAP_BY_VALUE = new HashMap<>();

  static {
    for (UtilitiesExpenseType type : UtilitiesExpenseType.values()) {
      MAP_BY_VALUE.put(type.value, type);
    }
  }

  UtilitiesExpenseType(String value, String labelSrc) {
    this.value = value;
    this.labelSrc = labelSrc;
  }

  public static String getLabelSrcFromValue(String value) {
    UtilitiesExpenseType expenseType = (UtilitiesExpenseType) MAP_BY_VALUE.get(value);
    return expenseType != null ? expenseType.labelSrc : null;
  }
}