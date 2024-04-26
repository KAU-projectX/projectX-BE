package com.projectX.projectX.domain.tour.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class ContentIdNotFoundException extends ApplicationException {

    public ContentIdNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
