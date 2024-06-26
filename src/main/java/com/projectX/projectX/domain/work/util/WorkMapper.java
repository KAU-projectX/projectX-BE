package com.projectX.projectX.domain.work.util;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.work.dto.response.WorkGetAllResponse;
import com.projectX.projectX.domain.work.dto.response.WorkGetDetailResponse;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkMapper {

    public static WorkGetAllResponse toWorkGetAllResponse(Cafe cafe) {
        return WorkGetAllResponse.builder()
            .id(cafe.getId())
            .name(cafe.getName())
            .address(cafe.getAddress())
            .phone(cafe.getPhoneNumber())
            .imageUrl("")
            .build();
    }

    public static WorkGetDetailResponse toWorkGetDetailResponse(Cafe cafe) {
        return WorkGetDetailResponse.builder()
            .id(cafe.getId())
            .address(cafe.getAddress())
            .name(cafe.getName())
            .uri(cafe.getUri())
            .phone(cafe.getPhoneNumber())
            .latitude(cafe.getLatitude())
            .longitude(cafe.getLongitude())
            .cafeType(cafe.getCafeType())
            .jejuRegion(cafe.getJejuRegion())
            .imageUrl(new ArrayList<>())
            .build();
    }

}
