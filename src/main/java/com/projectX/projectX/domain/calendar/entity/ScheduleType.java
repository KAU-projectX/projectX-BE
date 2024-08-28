package com.projectX.projectX.domain.calendar.entity;

import com.projectX.projectX.domain.calendar.exception.InvalidScheduleTypeException;
import com.projectX.projectX.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum ScheduleType {
    WORK("WORK"),
    TRAVEL("TRAVEL");

    private final String value;

    ScheduleType(String value){
        this.value = value;
    }

    public static ScheduleType toScheduleType(String calendarScheduleType) {
        for (ScheduleType scheduleType : values()) {
            if (scheduleType.value.equalsIgnoreCase(calendarScheduleType)) {
                return scheduleType;
            }
        }
        throw new InvalidScheduleTypeException(ErrorCode.INVALID_CALENDAR_TYPE_EXCEPTION);
    }

}
