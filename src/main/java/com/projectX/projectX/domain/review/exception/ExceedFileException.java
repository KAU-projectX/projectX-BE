package com.projectX.projectX.domain.review.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class ExceedFileException extends ApplicationException {

    public ExceedFileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
