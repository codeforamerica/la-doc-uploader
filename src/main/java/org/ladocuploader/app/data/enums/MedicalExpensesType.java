package org.ladocuploader.app.data.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum MedicalExpensesType {
  DENTAL("dentalBills", "medical-expenses.dental-bills"),
  HOSPITAL_BILLS("hospitalBills", "medical-expenses.hospital-bills"),
  PRESCRIPTION_MEDICINE("prescriptionMedicine", "medical-expenses.prescription-medicine"),
  PRESCRIPTION_PREMIUM("prescriptionPremium", "medical-expenses.prescription-premium"),
  MEDICAL_APPLIANCES("medicalAppliances", "medical-expenses.medical-appliances"),
  INSURANCE_PREMIUMS("insurancePremiums", "medical-expenses.insurance-premiums"),
  NURSING_HOME("nursingHome", "medical-expenses.nursing-home"),
  OTHER("otherMedicalExpenses","medical-expenses.other");


  private final String value;
  private final String labelSrc;

  static private final Map<String, MedicalExpensesType> MAP_BY_VALUE = new HashMap<>();

  static {
    for (MedicalExpensesType type : MedicalExpensesType.values()) {
      MAP_BY_VALUE.put(type.value, type);
    }
  }

  MedicalExpensesType(String value, String labelSrc) {
    this.value = value;
    this.labelSrc = labelSrc;
  }

  public static String getLabelSrcFromValue(String value) {
    MedicalExpensesType expenseType = (MedicalExpensesType) MAP_BY_VALUE.get(value);
    return expenseType != null ? expenseType.labelSrc : null;
  }
}