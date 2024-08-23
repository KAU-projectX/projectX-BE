package com.projectX.projectX.domain.review.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class AlreadyExistCafeReviewException extends ApplicationException {

    public AlreadyExistCafeReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
