package com.projectX.projectX.domain.cafe.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class UnsupportedEncodingTypeException extends ApplicationException {

    public UnsupportedEncodingTypeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
