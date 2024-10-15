package com.sparta.springtrello.domain.member.entity;

import com.sparta.springtrello.domain.user.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ROLE_CREATOR(Authority.CREATOR),
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
        public static final String CREATOR = "ROLE_CREATOR";
        public static final String READER = "ROLE_READER";
        public static final String DELETE = "ROLE_DELETE";
    }
}
