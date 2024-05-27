package com.projectX.projectX.domain.cafe.controller;

import com.projectX.projectX.domain.cafe.service.CSVReader;
import com.projectX.projectX.global.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "cafe api", description = "cafe 관련 API")
@Slf4j
@RestController
@RequestMapping("v1/cafe")
@RequiredArgsConstructor
public class CafeRestController {

    private final CSVReader csvReader;

    @GetMapping("/cafeInfo")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "카페 정보 저장 API", description = "전국 카페 정보 csv 파일을 통해 카페 정보를 디비에 저장하는 api입니다.")
    public ResponseDTO<String> createCafeInfo() {
        List<Map<String, String>> cafeList = csvReader.readCSV();
        csvReader.saveCafeInfo(cafeList);
        return ResponseDTO.res("카페 정보를 성공적으로 저장하였습니다.");
    }
}
