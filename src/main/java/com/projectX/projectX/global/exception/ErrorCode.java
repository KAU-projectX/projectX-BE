package com.projectX.projectX.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //MEMBER
    ALREADY_EXIST_ID_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 사용중인 아이디입니다."),
    INVALID_PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "비밀번호는 아이디와 같을 수 없습니다."),
    INVALID_MEMBER_EXCEPTION(HttpStatus.BAD_REQUEST, "해당하는 회원이 존재하지 않습니다."),
    INVALID_PROVIDER_TYPE_EXCEPTION(HttpStatus.BAD_REQUEST, "해당하는 Provider Type이 존재하지 않습니다."),

    //TOKEN
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),

    //TOUR API
    INVALID_URI_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않은 요청 URI입니다."),
    INVALID_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않은 API 요청입니다."),
    CONTENT_ID_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않은 콘텐트 ID입니다."),
    JEJU_REGION_NOT_FOUND(HttpStatus.BAD_REQUEST,
        "제주 지역을 찾을 수 없습니다."),
    INVALID_PROTOCOL_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않은 프로토콜 요청입니다."),
    UNSUPPORTED_ENCODING_EXCEPTION(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 인코딩입니다."),
    CANNOT_PARSE_JSON_EXCEPTION(HttpStatus.NOT_ACCEPTABLE,
        "json을 parse할 수 없습니다."),

    //Work
    WORK_NOT_FOUND(HttpStatus.NOT_FOUND, "work 정보를 찾을 수 없습니다."),

    //Travel
    TRAVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "travel 정보를 찾을 수 없습니다."),

    //Page
    NO_MORE_PAGE(HttpStatus.BAD_REQUEST, "더이상 페이지가 존재하지 않습니다."),
    INVALID_PAGE(HttpStatus.BAD_REQUEST, "유효하지 않은 페이지입니다.")
    ;//Error Code를 작성한 마지막에 ;를 추가합니다.

    private final HttpStatus httpStatus;
    private final String simpleMessage;

    ErrorCode(HttpStatus httpStatus, String simpleMessage) {
        this.httpStatus = httpStatus;
        this.simpleMessage = simpleMessage;
    }
}