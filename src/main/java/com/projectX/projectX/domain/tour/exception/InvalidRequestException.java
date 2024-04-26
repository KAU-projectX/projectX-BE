package com.projectX.projectX.domain.tour.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class InvalidRequestException extends ApplicationException {

    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
