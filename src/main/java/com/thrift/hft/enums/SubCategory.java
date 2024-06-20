package com.thrift.hft.enums;

public enum SubCategory {

    TSHIRT("T-Shirt"),
    SHIRT("Shirt"),
    HODDIE("Hoddie"),
    SHORTS("Shorts"),
    JEANS("Jeans"),
    TOPS("Tops"),
    SKIRTS("Skirt"),
    DRESSES("Dressed"),
    OTHERS("Others");

    private String value;

    SubCategory(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
