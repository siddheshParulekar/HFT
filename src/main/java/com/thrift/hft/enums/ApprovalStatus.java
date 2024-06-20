package com.thrift.hft.enums;

public enum ApprovalStatus {

    APPROVED("Approved"),
    REJECTED("Rejected"),
    PENDING("Pending");

    private String value;

    ApprovalStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
