package org.ladocuploader.app.data.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum HomeExpensesType {
  RENT("rent", "home-expenses.rent"),
  MORTGAGE("mortgage", "home-expenses.mortgage"),
  HOMEOWNERS_INSURANCE("homeownerInsurance", "home-expenses.homeinsurance"),
  PROPERTY_TAX("propertyTax", "home-expenses.tax"),
  CONDO_FEES("condominiumFees", "home-expenses.condo-fees"),
  LOT_RENT("lotRent", "home-expenses.lot-rent"),
  FLOOD_INSURANCE("floodInsurance", "home-expenses.flood-insurance"),
  OTHER("otherHomeExpenses", "home-expenses.other");

  private final String value;
  private final String labelSrc;

  static private final Map<String, HomeExpensesType> MAP_BY_VALUE = new HashMap<>();

  static {
    for (HomeExpensesType type : HomeExpensesType.values()) {
      MAP_BY_VALUE.put(type.value, type);
    }
  }

  HomeExpensesType(String value, String labelSrc) {
    this.value = value;
    this.labelSrc = labelSrc;
  }

  public static String getLabelSrcFromValue(String value) {
    HomeExpensesType expenseType = (HomeExpensesType) MAP_BY_VALUE.get(value);
    return expenseType != null ? expenseType.labelSrc : null;
  }
}