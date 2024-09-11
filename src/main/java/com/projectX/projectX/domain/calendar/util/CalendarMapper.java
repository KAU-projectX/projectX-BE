package com.projectX.projectX.domain.calendar.util;

import com.projectX.projectX.domain.calendar.dto.request.CalendarScheduleRequest;
import com.projectX.projectX.domain.calendar.entity.Calendar;
import com.projectX.projectX.domain.member.entity.Member;

public class CalendarMapper {

    public static Calendar toCalendar(Member member, CalendarScheduleRequest calendarScheduleRequest) {
        return Calendar.builder()
            .dateFrom(calendarScheduleRequest.dateFrom())
            .dateTo(calendarScheduleRequest.dateTo())
            .timeFrom(calendarScheduleRequest.timeFrom())
            .timeTo(calendarScheduleRequest.timeTo())
            .title(calendarScheduleRequest.title())
            .location(calendarScheduleRequest.location())
            .memo(calendarScheduleRequest.memo())
            .scheduleType(calendarScheduleRequest.scheduleType())
            .user(member).build();
    }

}
