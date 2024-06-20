package com.projectX.projectX.domain.travel.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class TravelNotFoundException extends ApplicationException {

    public TravelNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
