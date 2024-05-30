package com.projectX.projectX.domain.cafe.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class InvalidProtocolException extends ApplicationException {

    public InvalidProtocolException(ErrorCode errorCode) {
        super(errorCode);
    }
}
