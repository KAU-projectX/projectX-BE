package com.projectX.projectX.domain.review.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class CannotUploadFileException extends ApplicationException {

    public CannotUploadFileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
