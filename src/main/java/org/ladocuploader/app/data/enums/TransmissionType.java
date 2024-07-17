package org.ladocuploader.app.data.enums;

import org.ladocuploader.app.csv.enums.CsvPackageType;

public enum TransmissionType {
    SNAP(null),
    ECE_ORLEANS(CsvPackageType.ECE_ORLEANS_PACKAGE),
    ECE_JEFFERSON(CsvPackageType.ECE_JEFFERSON_PACKAGE),
    WIC(CsvPackageType.WIC_PACKAGE);

    private final CsvPackageType packageType;

    TransmissionType(CsvPackageType packageType){
        this.packageType = packageType;
    }

    public CsvPackageType getPackageType() {
        return this.packageType;
    }
}
