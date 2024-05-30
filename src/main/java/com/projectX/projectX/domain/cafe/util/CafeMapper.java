package com.projectX.projectX.domain.cafe.util;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.global.common.CafeType;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CafeMapper {

    public static Cafe toCafe(Map<String, String> cafeMap) {
        double latitude = Double.parseDouble(cafeMap.get("latitude"));
        double longitude = Double.parseDouble(cafeMap.get("longitude"));

        Integer cafeTypeNum = Integer.getInteger(cafeMap.get("cafeType"));
        CafeType cafeType = CafeType.fromInt(cafeTypeNum);

        return Cafe.builder()
            .cafeType(cafeType)
            .name(cafeMap.get("name"))
            .address(cafeMap.get("address"))
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }
}
