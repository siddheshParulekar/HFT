package com.thrift.hft.enums;

public enum SubCategoryM {

    TSHIRT("T-Shirt"),
    SHIRT("Shirt"),
    HODDIE("Hoddie"),
    SHORTS("Shorts"),
    JEANS("Jeans"),
    OTHERS("Others");


    private String value;

    SubCategoryM(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
