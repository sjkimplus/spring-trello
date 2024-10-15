package com.sparta.springtrello.domain.user.entity;

import com.sparta.springtrello.common.Timestamped;
import com.sparta.springtrello.common.exception.HotSixException;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.dto.request.SignUpRequestDto;
import com.sparta.springtrello.domain.user.enums.UserStatus;
import com.sparta.springtrello.domain.user.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.sparta.springtrello.common.exception.ErrorCode.USER_NO_AUTHORITY;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private User(Long id, String email, UserRole role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public User(SignUpRequestDto requestDto, String password) {
        this.email = requestDto.getEmail();
        this.password = password; // 암호화된 비밀번호
        this.name = requestDto.getName();
        this.role = requestDto.getRole();
        this.status = UserStatus.ACTIVATED;
    }

    public static User fromAuthUser(AuthUser authUser) {
        UserRole role = UserRole.of(
                authUser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElseThrow(() -> new HotSixException(USER_NO_AUTHORITY))
        );
        return new User(authUser.getId(), authUser.getEmail(), role);
    }

    public void deactivateUser() {
        this.status = UserStatus.DELETED; // check if the modified time is updated
    }
}