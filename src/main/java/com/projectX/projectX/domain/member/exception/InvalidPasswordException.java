package com.projectX.projectX.domain.member.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class InvalidPasswordException extends ApplicationException {

    public InvalidPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}
