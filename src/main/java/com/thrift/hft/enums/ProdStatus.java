package com.thrift.hft.enums;

public enum ProdStatus {

    IN_STOCK("In stock"),
    OUT_OF_STOCK("Out of stock" );

    private String value;

    ProdStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
