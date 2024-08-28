package com.projectX.projectX.domain.calendar.dto.request;

import com.projectX.projectX.domain.calendar.entity.ScheduleType;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;

public record CalendarScheduleRequest(
    @NotEmpty(message = "스케줄 시작일은 필수 입력 값입니다.")
    LocalDate dateFrom,

    @NotEmpty(message = "스케줄 종료일은 필수 입력 값입니다.")
    LocalDate dateTo,

    LocalTime timeFrom,
    LocalTime timeTo,

    @NotEmpty(message = "스케줄명은 필수 입력 값입니다.")
    String title,
    String location,
    String memo,
    ScheduleType scheduleType
) {

    @Builder
    public CalendarScheduleRequest{

    }

}
