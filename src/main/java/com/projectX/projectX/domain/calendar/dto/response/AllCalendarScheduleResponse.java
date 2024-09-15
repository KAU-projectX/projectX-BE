package com.projectX.projectX.domain.calendar.dto.response;

import com.projectX.projectX.domain.calendar.entity.ScheduleType;
import java.time.LocalDate;

public record AllCalendarScheduleResponse(
    String title,
    LocalDate dateFrom,
    LocalDate dateTo,
    ScheduleType scheduleType
) {

    public AllCalendarScheduleResponse(String title, LocalDate dateFrom, LocalDate dateTo,
        ScheduleType scheduleType) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.title = title;
        this.scheduleType = scheduleType;
    }

}
