package com.projectX.projectX.domain.work.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class InvalidPageException extends ApplicationException {

    public InvalidPageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
