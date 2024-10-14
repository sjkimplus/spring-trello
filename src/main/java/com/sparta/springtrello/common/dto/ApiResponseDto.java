//package com.sparta.springtrello.common.dto;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Getter
//@RequiredArgsConstructor
//public class ApiResponseDto<T> {
//
//    private enum Status {
//        SUCCESS, FAIL, ERROR
//    }
//
//    private final Status status;
//    private final T data;
//    private final String message;
//
//    public static <T> ApiResponseDto<T> success(T data) {
//        return new ApiResponseDto<>(Status.SUCCESS, data, "요청이 성공적으로 처리되었습니다");
//    }
//
//    public static ApiResponseDto<?> successWithNoContent() {
//        return new ApiResponseDto<>(Status.SUCCESS, null, "요청이 성공적으로 처리되었지만 내용이 없습니다");
//    }
//
//
//    public static ApiResponseDto<?> error(String message) {
//        return new ApiResponseDto<>(Status.FAIL, "", message);
//    }
//}