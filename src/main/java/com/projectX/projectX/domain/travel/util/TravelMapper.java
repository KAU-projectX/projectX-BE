package com.projectX.projectX.domain.travel.util;

import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.domain.travel.dto.response.TravelGetAllResponse;
import com.projectX.projectX.domain.travel.dto.response.TravelGetSpecificResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelMapper {

    public static TravelGetAllResponse toTravelGetAllResponse(Tour tour) {
        return TravelGetAllResponse.builder()
            .id(tour.getId())
            .name(tour.getTitle())
            .address(tour.getAddress())
            .phone(tour.getPhone())
            .imageUrl(tour.getImageUrl())
            .build();
    }

    public static TravelGetSpecificResponse toTravelGetSpecificResponse(Tour tour,
        List<String> tourImageList) {
        return TravelGetSpecificResponse.builder()
            .travelId(tour.getId())
            .name(tour.getTitle())
            .phone(tour.getPhone())
            .address(tour.getAddress())
            .homepageUrl(tour.getHomepageUrl())
            .kakaoMapUrl(tour.getKakaoMapUrl())
            .travelImageList(tourImageList)
            .overview(tour.getOverview())
            .build();
    }

}
