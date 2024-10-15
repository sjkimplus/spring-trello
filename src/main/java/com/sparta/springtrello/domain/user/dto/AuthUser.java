package com.sparta.springtrello.domain.user.dto;

import com.sparta.springtrello.domain.user.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {
    private final long id;
    private final String email;
//    private final UserRole role;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(long id, String email, UserRole role) {
        this.id = id;
        this.email = email;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
//        this.role = role;
    }
}
