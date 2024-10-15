package com.sparta.springtrello.domain.user.controller;

import com.sparta.springtrello.config.JwtUtil;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.dto.request.SignInRequestDto;
import com.sparta.springtrello.domain.user.dto.request.SignUpRequestDto;
import com.sparta.springtrello.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.springtrello.domain.user.dto.response.SignInResponseDto;
import com.sparta.springtrello.domain.user.dto.response.SignUpResponseDto;
import com.sparta.springtrello.domain.user.dto.response.UserSearchResponseDto;
import com.sparta.springtrello.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> create(@Valid @RequestBody SignUpRequestDto requestDto) {
        return ResponseEntity.ok(userService.create(requestDto));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> login(@Valid @RequestBody SignInRequestDto requestDto) {
        return ResponseEntity.ok(userService.login(jwtUtil, requestDto));
    }

    @DeleteMapping
    public String delete(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody UserDeleteRequestDto requestDto) {
        return userService.delete(authUser.getId(), requestDto);
    }

    @GetMapping
    public ResponseEntity<UserSearchResponseDto> find(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(userService.find(authUser.getId()));
    }
}

