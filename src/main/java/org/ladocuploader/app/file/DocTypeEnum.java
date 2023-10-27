package org.ladocuploader.app.file;

public enum DocTypeEnum {

    BIRTH_CERTIFICATE ("BirthCertificate", "Birth Certificate", "msg"),
    DRIVERS_LICENSE,
    SOCIAL_SECURITY_CARD,
    CHECK_STUB,
    OTHER_INCOME,
    BILL,
    MEDICAL_INFO,
    BANKING_INFO,
    MARRIAGE_LICENSE,
    DIVORCE_DECREE,
    COURT_ORDER,
    PATERNITY,
    OTHER;

    private final String value;
    private final String name;
    private final String msg;

    DocTypeEnum(String value, String name, String msg) {
        this.value = value;
        this.name = name;
        this.msg = msg;
    }

    public String getNameForValue(String value) {

    }


}
