package com.projectX.projectX.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodValidException(MethodArgumentNotValidException e) {
        String errorResponse = makeErrorResponse(e.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private String makeErrorResponse(BindingResult bindingResult) {
        String errorMessage = "";
        if (bindingResult.hasErrors()) {
            // DTO에 설정한 message
            errorMessage = bindingResult.getFieldError().getDefaultMessage();
        }
        return errorMessage;
    }
}
