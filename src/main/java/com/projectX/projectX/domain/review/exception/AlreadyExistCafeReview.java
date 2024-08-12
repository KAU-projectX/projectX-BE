package com.projectX.projectX.domain.review.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class AlreadyExistCafeReview extends ApplicationException {

    public AlreadyExistCafeReview(ErrorCode errorCode) {
        super(errorCode);
    }
}
