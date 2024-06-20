package com.projectX.projectX.domain.work.controller;

import com.projectX.projectX.domain.work.dto.response.WorkGetAllResponse;
import com.projectX.projectX.domain.work.dto.response.WorkGetDetailResponse;
import com.projectX.projectX.domain.work.service.WorkService;
import com.projectX.projectX.global.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "work api", description = "work 관련 API")
@RestController
@RequestMapping("v1/work")
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
        List<WorkGetAllResponse> cafeList = workService.getWorkAll(page, cafeType, jejuRegion,
            franchiseName);
        return ResponseDTO.res(cafeList, "work 조회에 성공했습니다.");
    }

    @GetMapping("/{cafe_id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "cafe 상세 정보 get API", description = "cafe 상세 정보를 get하는 API입니다.")
    public ResponseDTO<?> getCafeDetailInfo(
        @PathVariable @NotNull Long cafe_id
    ) {
        WorkGetDetailResponse dto = workService.getWorkDetailInfo(cafe_id);
        return ResponseDTO.res(dto, "work 상세 조회에 성공했습니다.");
    }

}
