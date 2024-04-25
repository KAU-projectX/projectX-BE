package com.projectX.projectX.global.common.security.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class AccessDeniedException extends ApplicationException {

    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
