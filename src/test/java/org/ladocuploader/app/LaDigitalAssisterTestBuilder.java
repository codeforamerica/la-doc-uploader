package org.ladocuploader.app;

import org.ladocuploader.app.inputs.LaDigitalAssister;

public class LaDigitalAssisterTestBuilder {

    private final LaDigitalAssister laDigitalAssister = new LaDigitalAssister();

    public LaDigitalAssister build() {
        return laDigitalAssister;
    }

    public LaDigitalAssisterTestBuilder validSinglePersonalHouseholdSNAP() {
        laDigitalAssister.setFirstName("Betty");
        laDigitalAssister.setLastName("White");

        return this;
    }


}
