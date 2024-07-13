package com.thrift.hft.enums;

public enum AddressType {


    HOME("Home"),
    WORK("Work");


    private String value;

    AddressType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
