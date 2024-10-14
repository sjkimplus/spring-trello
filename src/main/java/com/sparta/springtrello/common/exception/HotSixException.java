package com.sparta.springtrello.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HotSixException extends RuntimeException {

    private final HttpStatus status;

    public HotSixException(ErrorCode errorCode) {
        super(errorCode.getHttpStatus() + " " + errorCode.getMessage());
        this.status = errorCode.getHttpStatus();
    }
}