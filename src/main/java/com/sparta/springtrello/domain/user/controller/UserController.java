package com.sparta.springtrello.domain.user.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.config.JwtUtil;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.dto.request.SignInRequestDto;
import com.sparta.springtrello.domain.user.dto.request.SignUpRequestDto;
import com.sparta.springtrello.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.springtrello.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponseDto<?>> create(@Valid @RequestBody SignUpRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.create(requestDto)));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponseDto<?>> login(@Valid @RequestBody SignInRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).header("Authorization",(userService.login(jwtUtil, requestDto)).getToken()).build();
    }

    @DeleteMapping
    public ResponseEntity<ApiResponseDto<?>> delete(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody UserDeleteRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.delete(authUser.getId(), requestDto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<?>> find(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(ApiResponseDto.success(userService.find(authUser.getId())));
    }
}

