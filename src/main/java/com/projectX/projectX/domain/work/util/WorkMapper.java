package com.projectX.projectX.domain.work.util;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.work.dto.response.WorkGetAllResponse;
import com.projectX.projectX.domain.work.dto.response.WorkGetDetailResponse;
import com.projectX.projectX.domain.work.dto.response.WorkGetRecommdResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkMapper {

    public static List<WorkGetAllResponse> toWorkGetAllResponse(Page<Cafe> cafes) {
        List<WorkGetAllResponse> cafeList = new ArrayList<>();
        for (Cafe cafe : cafes) {
            WorkGetAllResponse workGetAllResponse = WorkMapper.toWorkGetResponse(cafe);
            cafeList.add(workGetAllResponse);
        }

        return cafeList;
    }

    private static WorkGetAllResponse toWorkGetResponse(Cafe cafe) {
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
