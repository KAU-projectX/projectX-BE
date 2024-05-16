package com.projectX.projectX.global.common;

public enum JejuRegion {
    JEJU(1), JOCHEON(2), SEONGSAN(3),
    JUNGMUN(4), ANDEOK(5), AEWOL(6);
    private final Integer value;

    JejuRegion(int value) {
        this.value = value;
    }

    public static JejuRegion fromInt(int value) {
        for (JejuRegion e : JejuRegion.values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for JEJU REGION: " + value);
    }
}
