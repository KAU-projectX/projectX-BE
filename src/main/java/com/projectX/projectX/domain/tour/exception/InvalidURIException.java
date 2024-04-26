package com.projectX.projectX.domain.tour.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class InvalidURIException extends ApplicationException {

    public InvalidURIException(ErrorCode errorCode) {
        super(errorCode);
    }
}
