package com.projectX.projectX.domain.tour.controller;

import com.projectX.projectX.domain.tour.service.TourService;
import com.projectX.projectX.global.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "tour api", description = "tour api 관련 API")
@RestController
@RequestMapping("v1/tour")
@RequiredArgsConstructor
public class TourRestController {

    private final TourService tourService;

    @GetMapping("/create/tour")
    @Operation(summary = "지역기반 정보 조회 API", description = "tour api를 통해 지역기반여행정보를 디비에 저장하는 api입니다.")
    public ResponseDTO<String> createTour() {
        tourService.createTour();
        return ResponseDTO.res("관광지 정보를 성공적으로 저장하였습니다.");
    }

    @GetMapping("/create/barrierFree")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "무장애 여행정보 생성 API", description = "tour api를 통해 무장애 여행정보를 디비에 저장하는 api 입니다.")
    public ResponseDTO<Void> createBarrierFree() {
        tourService.rotateForImpairment();
        return ResponseDTO.res("무장애 여행정보를 성공적으로 저장했습니다.");
    }

    @GetMapping("/create/tourImage")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "투어 이미지 생성 API", description = "tour api를 통해 투어 이미지를 디비에 저장하는 api 입니다.")
    public ResponseDTO<Void> createTourImage() {
        tourService.rotateForTourImage();
        return ResponseDTO.res("투어 이미지를 성공적으로 저장했습니다.");
    }

    @GetMapping("/create/detailCommon")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "관광지 상세정보 생성 API", description = "tour api를 통해 관광지 상세정보를 디비에 저장하는 api 입니다.")
    public ResponseDTO<Void> createDetailCommon() {
        tourService.rotateForEveryContentIdDetail();
        return ResponseDTO.res("관광지 상세 정보를 성공적으로 저장했습니다.");
    }

    @GetMapping("/create/detailCommon/kakao")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "관광지 추가정보 생성 API", description = "kakaoMap API 를 통해 관광지 추가정보를 디비에 저장하는 api 입니다.")
    public ResponseDTO<Void> createDetailCommonByKakao() {
        tourService.rotateForEmptyContentIdDetail();
        return ResponseDTO.res("관광지 추가 정보를 성공적으로 저장했습니다.");
    }

}