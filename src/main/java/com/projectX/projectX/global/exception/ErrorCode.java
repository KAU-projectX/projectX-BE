package com.projectX.projectX.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // MEMBER
    ALREADY_EXIST_ID_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 사용중인 아이디입니다."),
    INVALID_PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "비밀번호는 아이디와 같을 수 없습니다."),

    INVALID_AREACODE_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않은 지역코드입니다."),
    INVALID_SIGUNGU_CODE_EXCEPTION(HttpStatus.BAD_REQUEST,
        "유효하지 않은 시군구 코드입니다."); //Error Code를 작성한 마지막에 ;를 추가합니다.

    private final HttpStatus httpStatus;
    private final String simpleMessage;

    ErrorCode(HttpStatus httpStatus, String simpleMessage) {
        this.httpStatus = httpStatus;
        this.simpleMessage = simpleMessage;
    }
}
