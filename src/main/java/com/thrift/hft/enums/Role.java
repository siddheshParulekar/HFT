package com.thrift.hft.enums;

public enum Role {

    USER("User"),
    ADMIN("Admin");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }


}
