package com.sparta.springtrello.domain.user.dto.request;

import com.sparta.springtrello.domain.user.enums.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 올바르지 않습니다.")
    @NotNull(message = "필수 입력 항목입니다.")
    private String email;

    @Size(min = 8, message = "비밀번호는 8자리 이상으로 정해주세요")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함시켜주세요")
    @NotNull(message = "필수 입력 항목입니다.")
    private String password;

    @NotNull(message = "필수 입력 항목입니다.")
    private String name;

    @NotNull(message = "필수 입력 항목입니다.")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
