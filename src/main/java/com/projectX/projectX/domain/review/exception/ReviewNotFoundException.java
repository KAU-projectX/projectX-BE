package com.projectX.projectX.domain.review.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class ReviewNotFoundException extends ApplicationException {

    public ReviewNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
