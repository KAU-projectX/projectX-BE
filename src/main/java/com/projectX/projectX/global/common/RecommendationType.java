package com.projectX.projectX.global.common;

public enum RecommendationType {
    RECOMMEND(1), NEUTRAL(2), NOT_RECOMMEND(3);

    private final Integer value;

    RecommendationType(Integer value) {
        this.value = value;
    }

    public static RecommendationType fromInt(int value) {
        for (RecommendationType e : RecommendationType.values()) {
            if (e.value == value) {
                return e;
            }
        }

        throw new IllegalArgumentException("Invalid integer value for CafeType: " + value);
    }
}
