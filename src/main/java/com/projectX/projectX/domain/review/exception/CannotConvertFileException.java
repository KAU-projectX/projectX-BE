package com.projectX.projectX.domain.review.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class CannotConvertFileException extends ApplicationException {

    public CannotConvertFileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
