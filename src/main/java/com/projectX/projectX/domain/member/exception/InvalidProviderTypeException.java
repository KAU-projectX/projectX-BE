package com.projectX.projectX.domain.member.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class InvalidProviderTypeException extends ApplicationException {

    public InvalidProviderTypeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
