package com.projectX.projectX.domain.calendar.controller;

import com.projectX.projectX.domain.calendar.dto.request.CalendarScheduleRequest;
import com.projectX.projectX.domain.calendar.service.CalendarService;
import com.projectX.projectX.global.common.ResponseDTO;
import com.projectX.projectX.global.security.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "calendar api", description = "calendar 관련 API")
@RestController
@RequestMapping("v1/calendar")
@RequiredArgsConstructor
public class CalendarRestController {

    private final CalendarService calendarService;

    @PostMapping("/schedules")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "캘린더 스케줄링 정보 저장 API", description = "캘린더에서 스케줄링 정보를 저장하는 api 입니다.")
    public ResponseDTO<String> postCalendarSchedule (@Valid @RequestBody CalendarScheduleRequest calendarScheduleRequest,
        @AuthenticationPrincipal CustomOAuth2User user) {
        calendarService.createSchedule(calendarScheduleRequest, user.getEmail());
        return ResponseDTO.res("캘린더 스케줄링 정보 저장에 성공했습니다.");
    }


}
