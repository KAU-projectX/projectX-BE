package com.projectX.projectX.domain.tour.dto.request;

import com.projectX.projectX.domain.tour.entity.Sido;
import lombok.Builder;

public record TourSigunguStoreRequest(String sigunguName, Integer sigunguCode, Sido sido) {

    @Builder
    public TourSigunguStoreRequest {
    }
}
