package org.ladocuploader.app.file;

import java.util.Arrays;
import java.util.Optional;

public enum DocTypeEnum {
    BIRTH_CERTIFICATE ("BirthCertificate", "add-document-types.doc-type-label.permanent-documents.birth-certificate"),
    DRIVERS_LICENSE("DriversLicense", "add-document-types.doc-type-label.permanent-documents.drivers-license"),
    SOCIAL_SECURITY_CARD("SocialSecurityCard", "add-document-types.doc-type-label.permanent-documents.social-security-card"),
    CHECK_STUP("CheckStub", "add-document-types.doc-type-label.income.check-stub"),
    OTHER_INCOME("OtherIncome", "add-document-types.doc-type-label.income.other-income"),
    BILL("Bill", "add-document-types.doc-type-label.expenses.bills"),
    MEDICAL_INFO("MedicalInfo", "add-document-types.doc-type-label.medical.medical-info"),
    BANKING_INFO("BankingInfo", "add-document-types.doc-type-label.resources.banking-info"),
    MARRIAGE_LICENSE("MarriageLicense", "add-document-types.doc-type-label.legal.marriage-license"),
    DIVORCE_DECREE("DivorceDecree","add-document-types.doc-type-label.legal.divorce-decree"),
    COURT_ORDER("CourtOrder", "add-document-types.doc-type-label.legal.court-order"),
    PATERNITY("Paternity", "add-document-types.doc-type-label.paternity.ack-of-paternity"),
    OTHER("Other", "add-document-types.doc-type-label.correspondence.other");

    private final String value;
    private final String nameSrc;

    DocTypeEnum(String value, String nameSrc) {
        this.value = value;
        this.nameSrc = nameSrc;
    }

    public String getNameSrc() {
        return nameSrc;
    }

    public String getValue() {
        return value;
    }

    public static String findNameSrcForValue(String value) {
      Optional<DocTypeEnum> maybeEnum = Arrays.stream(DocTypeEnum.values())
          .filter(e -> e.value.equals(value))
          .findFirst();
      return maybeEnum.map(docTypeEnum -> docTypeEnum.nameSrc).orElse(null);
    }
}
