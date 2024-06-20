package com.projectX.projectX.domain.travel.util;

import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.domain.travel.dto.response.TravelGetAllResponse;
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

}
