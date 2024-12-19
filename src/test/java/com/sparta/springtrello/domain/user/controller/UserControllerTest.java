package com.sparta.springtrello.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springtrello.config.JwtSecurityFilter;
import com.sparta.springtrello.config.JwtUtil;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.dto.request.SignInRequestDto;
import com.sparta.springtrello.domain.user.dto.request.SignUpRequestDto;
import com.sparta.springtrello.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.springtrello.domain.user.dto.response.SignInResponseDto;
import com.sparta.springtrello.domain.user.dto.response.SignUpResponseDto;
import com.sparta.springtrello.domain.user.dto.response.UserSearchResponseDto;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void User_회원가입_성공() throws Exception {
        // Given
        SignUpRequestDto request = new SignUpRequestDto("username@email.com", "Password1!", "goodName", UserRole.ROLE_USER);
        SignUpResponseDto response = new SignUpResponseDto(request);
        when(userService.create(request)).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService, times(1)).create(any(SignUpRequestDto.class));
    }

    @Test
    void User_로그인_성공() throws Exception {
        // Given
        String mockToken = "mockJwtToken";
        User user = new User(1l, "user1@email.com", UserRole.ROLE_USER);
        SignInRequestDto request = new SignInRequestDto("username@email.com", "Password1!");
        SignInResponseDto response = new SignInResponseDto(user, mockToken);

        // when
        when(userService.login(any(JwtUtil.class), any(SignInRequestDto.class))).thenReturn(response);

        // then
        mockMvc.perform(post("/users/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", mockToken));

        verify(userService, times(1)).login(any(JwtUtil.class), any(SignInRequestDto.class));
    }

    @Test
    void User_삭제_성공() throws Exception {
        // Given
        UserDeleteRequestDto request = new UserDeleteRequestDto("Password123!");
        String result = "삭제 완료";
        AuthUser authUser = new AuthUser(1L, "user1@email.com", UserRole.ROLE_USER);


        // Mock the service call
        when(userService.delete(any(Long.class), any(UserDeleteRequestDto.class))).thenReturn(result);

        // When
        userController.delete(authUser, request);

        // Verify service interaction
        verify(userService, times(1)).delete(any(Long.class), any(UserDeleteRequestDto.class));
    }

    @Test
    void User_조회_성공() throws Exception {
        // Given
        AuthUser authUser = new AuthUser(1L, "user1@email.com", UserRole.ROLE_ADMIN);
        User staticUser = new User();
        User user = staticUser.fromAuthUser(authUser);
        UserSearchResponseDto result = new UserSearchResponseDto(user);

        // Mock the service call
        when(userService.find(any(Long.class))).thenReturn(result);

        // When
        userController.find(authUser);

        // Verify service interaction
        verify(userService, times(1)).find(any(Long.class));
    }
}
