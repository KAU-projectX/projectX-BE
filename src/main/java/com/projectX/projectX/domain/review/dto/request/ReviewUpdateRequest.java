package com.projectX.projectX.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewUpdateRequest(
    @Max(value = 5, message = "평점은 5점 이하여야 합니다.")
    @Min(value = 0, message = "평점은 0점 이상이어야 합니다.")
    Integer score,

    @Size(min = 10, max = 500, message = "리뷰는 10자에서 500자 사이로 입력해주세요")
    String contents,

    @NotNull(message = "추천 타입을 입력해주세요")
    Integer recommendType

) {

}
