package com.projectX.projectX.domain.member.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class InvalidMemberException extends ApplicationException {

    public InvalidMemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
