package com.projectX.projectX.domain.calendar.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class InvalidScheduleTypeException extends ApplicationException {

    public InvalidScheduleTypeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
