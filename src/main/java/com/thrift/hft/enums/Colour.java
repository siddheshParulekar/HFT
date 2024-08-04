package com.thrift.hft.enums;

public enum Colour {

    RED("Red"),
    BLUE("Blue"),
    GREEN("Green"),
    YELLOW("Yellow"),
    BLACK("Black"),
    WHITE("White"),
    PURPLE("Purple"),
    PINK("Pink"),
    BROWN("Brown"),
    BEIGE("Beige"),
    GREY("Grey"),
    OTHER("Other");


    private String value;

    Colour(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}