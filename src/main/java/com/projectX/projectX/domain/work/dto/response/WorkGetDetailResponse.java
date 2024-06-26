package com.projectX.projectX.domain.work.dto.response;

import com.projectX.projectX.global.common.CafeType;
import com.projectX.projectX.global.common.JejuRegion;
import java.util.List;
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
    String phone,
    List<String> imageUrl
) {


    @Builder
    public WorkGetDetailResponse {
    }

}
