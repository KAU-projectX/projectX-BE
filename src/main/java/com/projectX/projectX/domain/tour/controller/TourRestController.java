package com.projectX.projectX.domain.tour.controller;

import com.projectX.projectX.domain.tour.service.TourService;
import com.projectX.projectX.global.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "tour api", description = "tour api 관련 API")
@RestController
@RequestMapping("v1/tour")
@RequiredArgsConstructor
public class TourRestController {

    private final TourService tourService;

    @GetMapping("/create/sido")
    @Operation(summary = "시도 생성 API", description = "tour api를 통해 시도를 디비에 저장하는 api입니다.")
    public ResponseDTO<String> createSido() {
        return ResponseDTO.res(tourService.createSido());
    }

    @GetMapping("/create/sigungu")
    @Operation(summary = "시군구 생성 API", description = "tour api를 통해 시군구를 디비에 저장하는 api 입니다.")
    public ResponseDTO<String> createSigungu(@RequestParam Integer areaCode) {
        return ResponseDTO.res(tourService.createSigungu(areaCode));
    }

    @GetMapping("/create/tour")
    @Operation(summary = "지역기반 정보 조회 API", description = "tour api를 통해 지역기반여행정보를 디비에 저장하는 api입니다.")
    public ResponseDTO<String> createTour(@RequestParam Integer sido,
        @RequestParam Integer sigungu) {
        return ResponseDTO.res(tourService.createTour(sido, sigungu));
    }

}
