package com.projectX.projectX.global.exception;

public class ForbiddenException extends ApplicationException{

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
