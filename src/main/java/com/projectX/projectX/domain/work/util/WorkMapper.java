package com.projectX.projectX.domain.work.util;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.work.dto.response.WorkGetAllResponse;
import com.projectX.projectX.domain.work.dto.response.WorkGetDetailResponse;
import com.projectX.projectX.domain.work.dto.response.WorkGetRecommdResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkMapper {

    public static WorkGetAllResponse toWorkGetResponse(Cafe cafe, String imageUrl) {
        return WorkGetAllResponse.builder()
            .id(cafe.getId())
            .name(cafe.getName())
            .address(cafe.getAddress())
            .phone(cafe.getPhoneNumber())
            .imageUrl(imageUrl)
            .build();
    }

    public static WorkGetDetailResponse toWorkGetDetailResponse(Cafe cafe, List<String> images) {
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
            .imageUrl(images)
            .build();
    }

    public static List<WorkGetRecommdResponse> toWorkGetRecommendResponse(List<Cafe> cafes) {
        List<WorkGetRecommdResponse> returnCafes = new ArrayList<>();
        for (Cafe cafe : cafes) {
            WorkGetRecommdResponse cafeRecommendResponseDto = new WorkGetRecommdResponse(
                cafe.getId(), cafe.getName(), cafe.getAddress());
            returnCafes.add(cafeRecommendResponseDto);
        }

        return returnCafes;
    }

}
