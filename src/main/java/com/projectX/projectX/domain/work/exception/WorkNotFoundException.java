package com.projectX.projectX.domain.work.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class WorkNotFoundException extends ApplicationException {

    public WorkNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
