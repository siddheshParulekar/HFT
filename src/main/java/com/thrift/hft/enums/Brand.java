package com.thrift.hft.enums;

public enum Brand {

    HM("H&M"),
    ZARA("Zara"),
    THE_NORTH_FACE("The North Face"),
    COLUMBIA("Columbia"),
    NIKE("Nike"),
    ADIDAS("Adidas"),
    PUMA("Puma"),
    REEBOK("Reebok"),
    CARHARTT("Carhartt"),
    OTHERS("Others");




    private String value;

    Brand(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
