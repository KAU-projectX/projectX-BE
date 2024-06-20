package com.projectX.projectX.domain.travel.dto.response;

import lombok.Builder;

public record TravelGetAllResponse(
    Long id,
    String name,
    String address,
    String phone,
    String imageUrl
) {

    @Builder
    public TravelGetAllResponse {

    }

}
