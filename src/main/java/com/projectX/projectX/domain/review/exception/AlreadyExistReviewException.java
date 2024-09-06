package com.projectX.projectX.domain.review.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class AlreadyExistReviewException extends ApplicationException {

    public AlreadyExistReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
