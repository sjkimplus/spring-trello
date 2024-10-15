package com.sparta.springtrello.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String username;

    public UserResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
