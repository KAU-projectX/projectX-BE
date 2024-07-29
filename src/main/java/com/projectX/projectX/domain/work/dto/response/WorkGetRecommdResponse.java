package com.projectX.projectX.domain.work.dto.response;

import jakarta.validation.constraints.NotBlank;

public record WorkGetRecommdResponse(
    @NotBlank
    Long id,
    @NotBlank
    String name,
    @NotBlank
    String address
) {

}
