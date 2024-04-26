package com.projectX.projectX.domain.tour.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class InvalidSigunguCodeException extends ApplicationException {

    public InvalidSigunguCodeException(ErrorCode errorCode) {
        super(errorCode);
    }
}