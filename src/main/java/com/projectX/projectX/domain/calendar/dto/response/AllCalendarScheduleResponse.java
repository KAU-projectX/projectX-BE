package com.projectX.projectX.domain.calendar.dto.response;

import com.projectX.projectX.domain.calendar.entity.ScheduleType;
import lombok.Builder;

public record AllCalendarScheduleResponse(
    String title,
    int startDate,
    int endDate,
    ScheduleType scheduleType
) {

    @Builder
    public AllCalendarScheduleResponse {

    }

}
