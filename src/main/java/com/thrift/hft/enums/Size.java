package com.thrift.hft.enums;

public enum Size {

    S("s"),
    M("m"),
    L("l"),
    XL("xl"),
    XXL("xxl"),
    XXXL("xxxl"),
    FREE_SIZE("Free size");


    private String value;

    Size(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
