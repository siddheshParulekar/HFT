package com.thrift.hft.enums;

public enum SubCategoryW {

    TOPS("Tops"),
    TSHIRT("T-Shirt"),
    SHIRT("Shirt"),
    HODDIE("Hoddie"),
    SHORTS("Shorts"),
    JEANS("Jeans"),
    SKIRTS("Skirt"),
    DRESSES("Dressed"),
    OTHERS("Others");



    private String value;

    SubCategoryW(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
