package com.projectX.projectX.domain.tour.controller;

import com.projectX.projectX.domain.tour.dto.response.TourSidoStoreResponse;
import com.projectX.projectX.domain.tour.service.TourService;
import com.projectX.projectX.global.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.responses.ApiResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "tour api", description = "tour api 관련 API")
@RestController
@RequestMapping("v1/tour")
@RequiredArgsConstructor
public class TourRestController {
    private final TourService tourService;

    @GetMapping("/create/sido")
    @Operation(summary = "시도 생성 API", description = "tour api를 통해 시도를 디비에 저장하는 api입니다.")
    public ResponseDTO<String> createSido(){
        return ResponseDTO.res(tourService.createSido());
    }

    @GetMapping("/create/sigungu")
    @Operation(summary = "시군구 생성 API", description =  "tour api를 통해 시군구를 디비에 저장하는 api 입니다.")
    public ResponseDTO<String> createSigungu(@RequestParam Integer areaCode){
        return ResponseDTO.res(tourService.createSigungu(areaCode));
    }

}
