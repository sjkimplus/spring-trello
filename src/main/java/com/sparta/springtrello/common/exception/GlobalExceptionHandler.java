package com.sparta.springtrello.common.exception;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HotSixException.class)
    public ResponseEntity<ApiResponseDto<?>> handlerGlobalException(HotSixException e) {
        return new ResponseEntity<>(ApiResponseDto.error(e.getMessage()), e.getStatus());
    }
}