package com.thrift.hft.enums;

public enum Condition {


    NEW("New"),
    LIKE_NEW("Like new"),
    GOOD("Good"),
    POOR("Poor");



    private String value;

    Condition(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
