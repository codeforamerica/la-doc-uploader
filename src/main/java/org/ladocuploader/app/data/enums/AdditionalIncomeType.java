package org.ladocuploader.app.data.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum AdditionalIncomeType {
    SSI("SSI", "additional-income.choice1"),
    SOCIAL_SECURITY("SocialSecurity", "additional-income.choice2"),

    UNEMPLOYMENT("Unemployment", "additional-income.choice3"),

    WORKERS_COMPENSATION("WorkersCompensation", "additional-income.choice4"),

    CHILD_SUPPORT("ChildSupport", "additional-income.choice5"),

    VETERANS_BENEFITS("VeteransBenefits", "additional-income.choice6"),

    CONTRIBUTIONS("Contributions", "additional-income.choice7"),

    OTHER("Other", "general.other");

    private final String value;
    private final String labelSrc;

    static private final Map<String, AdditionalIncomeType> MAP_BY_VALUE = new HashMap<>();

    static {
        for (AdditionalIncomeType type : AdditionalIncomeType.values()) {
            MAP_BY_VALUE.put(type.value, type);
        }
    }

    AdditionalIncomeType(String value, String labelSrc) {
        this.value = value;
        this.labelSrc = labelSrc;
    }

    public static String getLabelSrcFromValue(String value) {
        AdditionalIncomeType expenseType = (AdditionalIncomeType) MAP_BY_VALUE.get(value);
        return expenseType != null ? expenseType.labelSrc : null;
    }
}
