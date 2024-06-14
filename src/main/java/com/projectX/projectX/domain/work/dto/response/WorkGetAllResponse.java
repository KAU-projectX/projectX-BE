package com.projectX.projectX.domain.work.dto.response;

import lombok.Builder;

public record WorkGetAllResponse(
    Long id,
    String name,
    String address,
    String phone
) {

    @Builder
    public WorkGetAllResponse {
    }
}
