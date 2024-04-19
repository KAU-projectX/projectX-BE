package com.projectX.projectX.domain.tour.dto.request;

import lombok.Builder;

public record TourSidoStoreRequest(String sidoName, Integer sidoCode) {

    @Builder
    public TourSidoStoreRequest {

    }
}
