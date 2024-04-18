package com.projectX.projectX.domain.member.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class AlreadyExistIdException extends ApplicationException {

    public AlreadyExistIdException(ErrorCode errorCode) {
        super(errorCode);
    }
}
