package com.projectX.projectX.domain.tour.dto.request;

import lombok.Builder;

public record TourStoreRequest(
    String address, String specAddress, Sido sido, Sigungu sigungu,
    Long contentId, Integer contentTypeId, Long zipCode, String imageUrl, String thumbnailImageUrl,
    Float mapX, Float mapY, String title, String phone) {

    @Builder
    public TourStoreRequest {
    }
}
