package com.projectX.projectX.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewUpdateRequest(
    @Max(value = 5, message = "평점은 5점 이하여야 합니다.")
    @Min(value = 0, message = "평점은 0점 이상이어야 합니다.")
    @NotNull(message = "평점을 입력해주세요")
    Integer score,

    @Size(min = 10, max = 500, message = "리뷰는 10자에서 500자 사이로 입력해주세요")
    String contents,

    @Max(value = 3, message = "리뷰 타입은 1에서 3 사이로 입력해주세요")
    @Min(value = 1, message = "리뷰 타입은 1에서 3 사이로 입력해주세요")
    @NotNull(message = "추천 타입을 입력해주세요")
    Integer recommendType

) {

}
