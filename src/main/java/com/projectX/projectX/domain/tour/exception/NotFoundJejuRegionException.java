package com.projectX.projectX.domain.tour.exception;

import com.projectX.projectX.global.exception.ApplicationException;
import com.projectX.projectX.global.exception.ErrorCode;

public class NotFoundJejuRegionException extends ApplicationException {

    public NotFoundJejuRegionException(ErrorCode errorCode) {
        super(errorCode);
    }
}