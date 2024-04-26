package com.projectX.projectX.domain.tour.dto.request;

import lombok.Builder;

public record TourImpairmentRequest(
    int wheelChair,
    int brailleBlock,
    int audioGuide,
    int videoGuide,
    int stroller,
    int lactationRoom
) {

    @Builder
    public TourImpairmentRequest {
    }
}
