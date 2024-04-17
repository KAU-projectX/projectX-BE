package com.projectX.projectX.global.exception;

public class InternalServerException extends ApplicationException {

    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
