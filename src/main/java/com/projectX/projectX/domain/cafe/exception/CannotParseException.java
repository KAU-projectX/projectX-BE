package com.projectX.projectX.domain.cafe.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class CannotParseException extends ApplicationException {

    public CannotParseException(ErrorCode errorCode) {
        super(errorCode);
    }
}
