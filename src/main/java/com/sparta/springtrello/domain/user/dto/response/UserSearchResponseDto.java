package com.sparta.springtrello.domain.user.dto.response;

import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.enums.UserStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserSearchResponseDto {
    private String email;
    private String name;
    private UserRole userRole;
    private UserStatus userStatus;
    private LocalDateTime createdAt;

    public UserSearchResponseDto(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.userRole = user.getRole();
        this.userStatus = user.getStatus();
        this.createdAt = user.getCreatedAt();
    }
}
