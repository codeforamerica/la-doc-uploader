package org.ladocuploader.app.data.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum MoneyOnHandType {
  CHECKING("Checking account", "moneyonhand-types.option1"),
  SAVINGS("Savings account", "moneyonhand-types.option2"),
  JOINT("Joint account", "moneyonhand-types.option3"),
  BONDS("Bonds", "moneyonhand-types.option4"),
  CASH("Cash on hand", "moneyonhand-types.option5"),
  CD("Certificate of Deposit (CD)", "moneyonhand-types.option6"),
  MONEY_MARKET("Money Market Account", "moneyonhand-types.option7"),
  MUTUAL_FUNDS("Mutual funds", "moneyonhand-types.option8"),
  SAVINGS_BOND("Savings bond", "moneyonhand-types.option9"),
  STOCKS("Stocks", "moneyonhand-types.option10");


  private final String value;
  private final String labelSrc;

  static private final Map<String, MoneyOnHandType> MAP_BY_VALUE = new HashMap<>();

  static {
    for (MoneyOnHandType type : MoneyOnHandType.values()) {
      MAP_BY_VALUE.put(type.value, type);
    }
  }

  MoneyOnHandType(String value, String labelSrc) {
    this.value = value;
    this.labelSrc = labelSrc;
  }

  public static String getLabelSrcFromValue(String value) {
    MoneyOnHandType expenseType = (MoneyOnHandType) MAP_BY_VALUE.get(value);
    return expenseType != null ? expenseType.labelSrc : null;
  }
}