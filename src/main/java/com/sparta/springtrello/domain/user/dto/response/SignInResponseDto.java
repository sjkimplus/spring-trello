package com.sparta.springtrello.domain.user.dto.response;

import com.sparta.springtrello.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class SignInResponseDto {
    private String name;
    private String email;
    private String token;

    public SignInResponseDto(User user, String token) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.token = token;
    }
}
