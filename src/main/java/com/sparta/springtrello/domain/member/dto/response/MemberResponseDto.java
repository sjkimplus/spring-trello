package com.sparta.springtrello.domain.member.dto.response;

import com.sparta.springtrello.domain.user.dto.response.UserResponse;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private final Long id;
    private final UserResponse user;

    public MemberResponseDto(Long id, UserResponse user) {
        this.id = id;
        this.user = user;
    }
}
