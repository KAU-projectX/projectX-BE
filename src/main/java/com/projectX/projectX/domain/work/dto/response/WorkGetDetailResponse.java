package com.projectX.projectX.domain.work.dto.response;

import com.projectX.projectX.global.common.CafeType;
import com.projectX.projectX.global.common.JejuRegion;
import lombok.Builder;

public record WorkGetDetailResponse(
    Long id,
    String name,
    CafeType cafeType,
    String address,
    double latitude,
    double longitude,
    String uri,
    JejuRegion jejuRegion,
    String phone
) {


    @Builder
    public WorkGetDetailResponse {
    }

}
