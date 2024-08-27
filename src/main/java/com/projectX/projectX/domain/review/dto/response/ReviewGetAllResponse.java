package com.projectX.projectX.domain.review.dto.response;

import com.projectX.projectX.global.common.RecommendationType;
import java.util.List;
import lombok.Builder;

public record ReviewGetAllResponse(
    String userNickname,
    Integer score,
    String contents,
    List<String> images,
    RecommendationType recommendationType
) {

    @Builder
    public ReviewGetAllResponse {
    }
}
