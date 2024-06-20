package com.projectX.projectX.domain.travel.dto.response;

import java.util.List;
import lombok.Builder;

public record TravelGetSpecificResponse(
    long travelId,
    String name,
    String phone,
    String address,
    List<String> travelImageList,
    String homepageUrl,
    String kakaoMapUrl,
    String overview
) {

    @Builder
    public TravelGetSpecificResponse {
    }

}
