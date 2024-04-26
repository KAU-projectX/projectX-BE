package com.projectX.projectX.domain.tour.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class InvalidAreaCodeException extends ApplicationException {

    public InvalidAreaCodeException(ErrorCode errorCode) {
        super(errorCode);
    }
}