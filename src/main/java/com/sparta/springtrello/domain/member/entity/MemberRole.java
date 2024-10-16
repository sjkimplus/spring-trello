package com.sparta.springtrello.domain.member.entity;

import com.sparta.springtrello.domain.user.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ROLE_WORKSPACE(Authority.WORKSPACE),
    ROLE_BOARD(Authority.BOARD),
    ROLE_READER(Authority.READER),
    ROLE_DELETE(Authority.DELETE);

    private final String memberRole;

    public static MemberRole of(String role) {
        return Arrays.stream(MemberRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 MemberRole"));
    }

    public static class Authority {
        public static final String WORKSPACE = "ROLE_WORKSPACE";
        public static final String BOARD = "ROLE_BOARD";
        public static final String READER = "ROLE_READER";
        public static final String DELETE = "ROLE_DELETE";
    }
}
