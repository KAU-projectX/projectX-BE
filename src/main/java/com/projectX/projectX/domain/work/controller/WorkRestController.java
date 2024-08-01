package com.projectX.projectX.domain.work.controller;

import com.projectX.projectX.domain.work.dto.response.WorkGetAllResponse;
import com.projectX.projectX.domain.work.dto.response.WorkGetDetailResponse;
import com.projectX.projectX.domain.work.service.WorkService;
import com.projectX.projectX.global.common.JejuRegion;
import com.projectX.projectX.global.common.ResponseDTO;
import com.projectX.projectX.global.security.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "work api", description = "work 관련 API")
@RestController
@RequestMapping("v1/works")
@RequiredArgsConstructor
public class WorkRestController {

    private final WorkService workService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "work 정보 get API", description = "work 정보를 get 하는 API입니다.")
    public ResponseDTO<?> createCafeInfo(
        @RequestParam @NotNull Integer page,
        @RequestParam @NotNull Integer cafeType,
        @RequestParam(required = false) Integer jejuRegion,
        @RequestParam(required = false) String franchiseName
    ) {
        List<WorkGetAllResponse> cafeList = workService.getWorkAllInfo(page, cafeType, jejuRegion,
            franchiseName);
        return ResponseDTO.res(cafeList, "work 조회에 성공했습니다.");
    }

    @GetMapping("/{cafe_id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "work 상세 정보 get API", description = "cafe 상세 정보를 get하는 API입니다.")
    public ResponseDTO<?> getWorkDetailInfo(
        @PathVariable("cafe_id") @NotNull Long cafe_id
    ) {
        WorkGetDetailResponse dto = workService.getWorkDetailInfo(cafe_id);
        return ResponseDTO.res(dto, "work 상세 조회에 성공했습니다.");
    }

    @PostMapping("/{cafe_id}/scrap")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "work 스크랩 API", description = "work 게시물을 스크랩하는 API입니다.")
    public ResponseDTO<?> postWorkScrapInfo(
        @PathVariable("cafe_id") @NotNull Long cafeId,
        @AuthenticationPrincipal CustomOAuth2User user
    ) {
        String result = workService.postWorkScrapInfo(cafeId, user.getEmail());
        return ResponseDTO.res(result);
    }

    @GetMapping("/recommend")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "work 추천 API", description = "work, tour에서 사용하는 카페 추천 API 입니다.")
    public ResponseDTO<?> getWorkRecommendInfo(
        @RequestParam JejuRegion jejuRegion
    ) {
        return ResponseDTO.res(workService.getWorkRecommendInfo(jejuRegion), "추천 정보 조회에 성공했습니다.");
    }

}
