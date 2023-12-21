package org.ladocuploader.app.data.enums;

import org.ladocuploader.app.csv.enums.CsvPackageType;

public enum TransmissionType {
    SNAP(null),
    ECE(CsvPackageType.ECE_PACKAGE),
    WIC(CsvPackageType.WIC_PACKAGE);

    private final CsvPackageType packageType;

    TransmissionType(CsvPackageType packageType){
        this.packageType = packageType;
    }

    public CsvPackageType getPackageType() {
        return this.packageType;
    }
}
