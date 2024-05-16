package com.projectX.projectX.global.common;

public enum ContentType {
    TOUR(12), CULTURE(14), FESTIVAL(15), SPORTS(28),
    ACCOMMODATION(32), SHOPPING(38), RESTAURANT(39);

    private final Integer value;

    ContentType(int value) {
        this.value = value;
    }

    // 정수를 ENUM으로 변환하는 메서드
    public static ContentType fromInt(int value) {
        for (ContentType e : ContentType.values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for ContentType: " + value);
    }
}
