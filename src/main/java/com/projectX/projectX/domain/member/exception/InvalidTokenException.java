package com.projectX.projectX.domain.member.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class InvalidTokenException extends ApplicationException {

    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
