package com.projectX.projectX.domain.travel.controller;

import com.projectX.projectX.domain.travel.dto.response.TravelGetAllResponse;
import com.projectX.projectX.domain.travel.service.TravelService;
import com.projectX.projectX.global.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "travel api", description = "travel 관련 API")
@RestController
@RequestMapping("v1/travel")
@RequiredArgsConstructor
public class TravelRestController {

    private final TravelService travelService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "travel 전체 정보 조회 API", description = "travel 전체 정보를 조회하는 API입니다.")
    public ResponseDTO<?> getAllTravelInfo(
        @RequestParam @NotNull Integer page,
        @RequestParam @NotNull Integer contentType,
        @RequestParam(required = false) Integer jejuRegion
    ) {
        List<TravelGetAllResponse> travelList = travelService.getAllTravelInfo(page, contentType,
            jejuRegion);
        return ResponseDTO.res(travelList, "travel 전체 조회에 성공했습니다.");
    }

}
