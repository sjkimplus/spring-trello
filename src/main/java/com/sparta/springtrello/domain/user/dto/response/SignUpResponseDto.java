package com.sparta.springtrello.domain.user.dto.response;

import com.sparta.springtrello.domain.user.dto.request.SignUpRequestDto;
import com.sparta.springtrello.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResponseDto {
    private String email;
    private String password;
    private String name;
    private UserRole role;

    public SignUpResponseDto(SignUpRequestDto signUpRequestDto) {
        this.email = signUpRequestDto.getEmail();
        this.password = signUpRequestDto.getPassword();
        this.name = signUpRequestDto.getName();
        this.role = signUpRequestDto.getRole();
    }
}
