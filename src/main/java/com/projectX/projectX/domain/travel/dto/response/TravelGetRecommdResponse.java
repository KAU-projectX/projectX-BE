package com.projectX.projectX.domain.travel.dto.response;

import jakarta.validation.constraints.NotBlank;

public record TravelGetRecommdResponse(
    @NotBlank
    Long id,

    @NotBlank
    String name,

    @NotBlank
    String address
) {

}
