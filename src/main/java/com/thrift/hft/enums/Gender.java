package com.thrift.hft.enums;

public enum Gender {


    MEN("Men"),
    WOMEN("Women"),
    NA("NA");


    private String value;

    Gender(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
