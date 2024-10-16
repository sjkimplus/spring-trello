package com.sparta.springtrello.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 서버에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부서버 에러"),
    TOKEN_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "토큰을 찾지 못함"),
    // format error
    WRONG_FORMAT(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 올바른 양식이 아닙니다"),
    MISSING_FORMAT(HttpStatus.BAD_REQUEST, "모든 양식 값을 채워주세요"),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저 입니다."),
    USER_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저 타입입니다."),
    USER_ID_DUPLICATION(HttpStatus.BAD_REQUEST, "중복되는 아이디 입니다."),
    USER_PW_ERROR(HttpStatus.BAD_REQUEST, "비밀 번호가 아이디와 일치하지 않습니다."),
    USER_SAME_PW_ERROR(HttpStatus.NOT_FOUND, "현재와 동일한 비밀번호로 변경할 수 없습니다"),

    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 보드 입니다."),
    KANBAN_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 칸반 입니다."),
    WORKSPACE_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 워크스페이스 입니다."),
    MEMBER_RESIST_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 등록된 멤버입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 매니저 입니다."),
    KANBAN_SAME_ORDER(HttpStatus.BAD_REQUEST,"기존의 순서와 같은 순서입니다."),
    USER_NO_AUTHORITY(HttpStatus.UNAUTHORIZED, "권한불가한 접근 입니다"),
    DO_NOT_INVITATION_WORKSPACE(HttpStatus.NOT_FOUND,"존재하지 않는 멤버입니다."),

    // JWT
    JWT_UNSAVABLE(HttpStatus.BAD_REQUEST, "JWT 토큰을 쿠키에 저장하는데 실패 했습니다."),
    JWT_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않는 JWT 토큰 입니다"),
    JWT_EXPIRED(HttpStatus.BAD_REQUEST, "만료된 토큰 입니다."),
    JWT_TYPE_ERROR(HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰 입니다."),

    // Ticket
    TICKET_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 티켓입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 댓글입니다."),

    // 첨부파일
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND,"파일이 없습니다."),
    FILE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "파일 크기가 5MB를 초과합니다."),
    FILE_WRONG_FORMAT(HttpStatus.BAD_REQUEST, "지원되지 않는 파일 형식입니다.");


    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
