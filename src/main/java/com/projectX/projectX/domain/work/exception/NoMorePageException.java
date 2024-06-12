package com.projectX.projectX.domain.work.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class NoMorePageException extends ApplicationException {

    public NoMorePageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
