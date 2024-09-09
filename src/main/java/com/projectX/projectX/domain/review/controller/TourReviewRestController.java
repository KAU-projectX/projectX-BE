package com.projectX.projectX.domain.review.controller;

import com.projectX.projectX.domain.review.dto.request.ReviewUpdateRequest;
import com.projectX.projectX.domain.review.dto.response.ReviewGetAllResponse;
import com.projectX.projectX.domain.review.service.TourReviewService;
import com.projectX.projectX.domain.review.entity.RecommendationType;
import com.projectX.projectX.global.common.ResponseDTO;
import com.projectX.projectX.global.security.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/travels/{travel_id}/reviews")
@Tag(name = "tour review api", description = "관광지 리뷰 관련 API")
public class TourReviewRestController {

    private final TourReviewService tourReviewService;

    @PostMapping(consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "관광지 리뷰 등록 API", description = "관광지 리뷰를 등록하는 API 입니다.")
    public ResponseDTO<String> createTourReview(
        @PathVariable("travel_id") @NotNull Long travelId,
        @RequestParam(value = "multipartFile", required = false) List<MultipartFile> files,
        @RequestParam @Min(0) @Max(5) Integer score,
        @RequestParam @Size(min = 10, max = 500) String contents,
        @RequestParam RecommendationType recommendationType,
        @AuthenticationPrincipal CustomOAuth2User user
    ) {
        tourReviewService.createTourReview(travelId, files, score, contents, recommendationType,
            user.getEmail());
        return ResponseDTO.res("관광지 리뷰를 성공적으로 저장했습니다.");
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "관광지 리뷰 조회 API", description = "관광지 게시물 별 리뷰를 조회하는 API 입니다.")
    public ResponseDTO<?> getTravelReviewAllInfo(
        @PathVariable("travel_id") @NotNull Long travelId,
        @RequestParam Integer page
    ) {
        List<ReviewGetAllResponse> reviews = tourReviewService.getTourReviewAll(travelId, page);
        return ResponseDTO.res(reviews, "관광지 리뷰 조회를 완료하였습니다.");
    }

    @PatchMapping("/{review_id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "관광지 리뷰 수정 API", description = "관광지 게시물의 리뷰를 수정하는 API입니다.")
    public ResponseDTO<?> updateTravelReview(
        @PathVariable("travel_id") @NotNull Long cafeId,
        @PathVariable("review_id") @NotNull Long reviewId,
        @RequestBody ReviewUpdateRequest request,
        @AuthenticationPrincipal CustomOAuth2User user
    ) {
        tourReviewService.updateTourReview(cafeId, reviewId, request, user.getEmail());
        return ResponseDTO.res("관광지 리뷰를 성공적으로 업데이트했습니다.");
    }


}
