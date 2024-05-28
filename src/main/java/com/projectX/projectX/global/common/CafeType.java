package com.projectX.projectX.global.common;

public enum CafeType {
    BOOK(1), PERSONAL(2), FRANCHISE(3), LIBRARY(4);

    private final Integer value;

    CafeType(int value) {
        this.value = value;
    }

    public static CafeType fromInt(int value) {
        for (CafeType e : CafeType.values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for CafeType: " + value);
    }
}
