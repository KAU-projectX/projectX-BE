package com.projectX.projectX.domain.calendar.dto.request;

import com.projectX.projectX.domain.calendar.entity.ScheduleType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;

public record CalendarScheduleRequest(
    @NotNull(message = "스케줄 시작일은 필수 입력 값입니다.")
    LocalDate dateFrom,

    @NotNull(message = "스케줄 종료일은 필수 입력 값입니다.")
    LocalDate dateTo,

    LocalTime timeFrom,
    LocalTime timeTo,

    @NotNull(message = "스케줄명은 필수 입력 값입니다.")
    String title,
    String location,
    String memo,
    ScheduleType scheduleType
) {

    @Builder
    public CalendarScheduleRequest{

    }

}
