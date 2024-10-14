package com.sparta.springtrello.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE(UserStatus.Authority.ACTIVE),
    DELETED(UserStatus.Authority.DELETED);

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getUserStatus() {
        return this.status;
    }

    public static class Authority {
        public static final String ACTIVE = "USER_ACTIVE";
        public static final String DELETED = "USER_DELETED";
    }
}
