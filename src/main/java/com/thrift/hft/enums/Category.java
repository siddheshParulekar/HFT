package com.thrift.hft.enums;

public enum Category {

    MEN("Men"),
    WOMEN("Women"),
    UNISEX("Unisex");


    private String value;

    Category(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
