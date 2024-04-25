package com.projectX.projectX.global.common.security.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class InvalidRefreshTokenException extends ApplicationException {

    public InvalidRefreshTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}

