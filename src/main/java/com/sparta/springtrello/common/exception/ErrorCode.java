package com.sparta.springtrello.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저 입니다."),
    USER_ID_DUPLICATION(HttpStatus.BAD_REQUEST, "중복되는 아이디 입니다."),
    USER_PW_ERROR(HttpStatus.BAD_REQUEST, "비밀 번호가 아이디와 일치하지 않습니다."),
    USER_SAME_PW_ERROR(HttpStatus.NOT_FOUND, "현재와 동일한 비밀번호로 변경할 수 없습니다"),
    USER_NO_AUTHORITY(HttpStatus.UNAUTHORIZED, "권한불가한 접근 입니다"),

    // JWT
    JWT_UNSAVABLE(HttpStatus.BAD_REQUEST, "JWT 토큰을 쿠키에 저장하는데 실패 했습니다.");





    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
