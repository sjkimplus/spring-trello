package com.sparta.springtrello.domain.user.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVATED(UserStatus.Authority.ACTIVATED),
    DELETED(UserStatus.Authority.DELETED);

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

    public static class Authority {
        public static final String ACTIVATED = "USER_ACTIVATED";
        public static final String DELETED = "USER_DELETED";
    }
}
