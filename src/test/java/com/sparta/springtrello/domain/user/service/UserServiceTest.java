package com.sparta.springtrello.domain.user.service;

import com.sparta.springtrello.common.exception.HotSixException;
import com.sparta.springtrello.config.JwtUtil;
import com.sparta.springtrello.domain.user.dto.request.SignInRequestDto;
import com.sparta.springtrello.domain.user.dto.request.SignUpRequestDto;
import com.sparta.springtrello.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.springtrello.domain.user.dto.response.SignInResponseDto;
import com.sparta.springtrello.domain.user.dto.response.SignUpResponseDto;
import com.sparta.springtrello.domain.user.dto.response.UserSearchResponseDto;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.enums.UserStatus;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private User user;

    @InjectMocks
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    SignUpRequestDto signUpRequest;

    @BeforeEach
    public void setup(){
        signUpRequest = new SignUpRequestDto("name@email.com", "Password123!", "GoodName", UserRole.ROLE_USER);
    }

    @Test
    void User_생성_성공() {
        // Given
        String rawPassword = "Password123!";
        String encodedPassword = "encodedPassword";

        // Mocking password encoding and email check
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.empty());

        // When
        SignUpResponseDto response = userService.create(signUpRequest);

        // Then
        assertEquals("name@email.com", response.getEmail());
        assertEquals(rawPassword, response.getPassword());
    }

    @Test
    void User_생성_실패_양식공백() {
        // Given
        SignUpRequestDto request = new SignUpRequestDto("name@email.com", "Password123!", "", UserRole.ROLE_USER);

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.create(request) // Code that triggers the exception
        );

        // Optional: Verify the exception message or error code
        assertEquals("400 BAD_REQUEST 모든 양식 값을 채워주세요", exception.getMessage());
    }

    @Test
    void User_생성_실패_잘못된_양식() {
        // Given
        SignUpRequestDto request = new SignUpRequestDto("nameemail.com", "Password123!", "goodName", UserRole.ROLE_USER);

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.create(request) // Code that triggers the exception
        );

        // Optional: Verify the exception message or error code
        assertEquals("400 BAD_REQUEST 이메일 또는 비밀번호가 올바른 양식이 아닙니다", exception.getMessage());
    }

    @Test
    void User_생성_실패_이메일_중복() {
        // Given
        User existingUser = new User();

        // when
        // Mocking an existing user for email duplication
        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.of(existingUser));

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.create(signUpRequest) // Code that triggers the exception
        );

        // Optional: Verify the exception message or error code
        assertEquals("400 BAD_REQUEST 중복되는 아이디 입니다.", exception.getMessage());
    }

    @Test
    void User_로그인_성공() {
        // Given
        User user = new User(signUpRequest,"Password123!");
        SignInRequestDto signInRequest = new SignInRequestDto("name@email.com", "Password123!");

        String token = "jwtToken";

        // Mocking repository and password encoding behavior
        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole())).thenReturn(token);

        // When
        SignInResponseDto response = userService.login(jwtUtil, signInRequest);

        // Then
        assertEquals(signUpRequest.getEmail(), response.getEmail());
    }

    @Test
    void User_로그인_실패_유저_존재하지_않음() {
        // Given
        SignInRequestDto signInRequest = new SignInRequestDto("name123@email.com", "Password123!");

        // Mocking repository to return empty
        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.empty());

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.login(jwtUtil, signInRequest) // Code that triggers the exception
        );

        // Verify
        assertEquals("404 NOT_FOUND 존재하지 않는 유저 입니다.", exception.getMessage());
    }

    @Test
    void User_로그인_실패_삭제된_유저() {
        // given
        SignInRequestDto signInRequest = new SignInRequestDto("name@email.com", "Password123!");

        // Mocking repository to return a user
        when(userRepository.findByEmail("name@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(user.getStatus()).thenReturn(UserStatus.DELETED);

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.login(jwtUtil, signInRequest) // Code that triggers the exception
        );

        // Verify
        assertEquals("404 NOT_FOUND 존재하지 않는 유저 입니다.", exception.getMessage());
    }

    @Test
    void User_로그인_실패_비밀번호_불일치() {
        // given
        SignInRequestDto signInRequest = new SignInRequestDto("name@email.com", "Password123!");

        // Mocking repository to return a user
        when(userRepository.findByEmail("name@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())).thenReturn(false);

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.login(jwtUtil, signInRequest) // Code that triggers the exception
        );

        // Verify
        assertEquals("400 BAD_REQUEST 비밀 번호가 아이디와 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    void User_삭제_성공() {
        // Given
        Long userId = 1L;
        String currentPassword = "Password123!";
        String encodedPassword = "encodedPassword123!";
        User user = new User(signUpRequest, encodedPassword);
        UserDeleteRequestDto userDeleteRequest = new UserDeleteRequestDto(currentPassword);

        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userDeleteRequest.getPassword(), user.getPassword())).thenReturn(true);

        // then
        String response = userService.delete(userId, userDeleteRequest);

        // verify
        assertEquals("삭제 완료", response);
    }

    @Test
    void User_삭제_실패_이미삭제된_유저() {
        // given
        String currentPassword = "Password123!";
        UserDeleteRequestDto userDeleteRequest = new UserDeleteRequestDto(currentPassword);

        // Mocking repository to return a user
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        when(user.getStatus()).thenReturn(UserStatus.DELETED);

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.delete(1l, userDeleteRequest) // Code that triggers the exception
        );

        // Verify
        assertEquals("404 NOT_FOUND 존재하지 않는 유저 입니다.", exception.getMessage());
    }

    @Test
    void User_삭제_실패_없는_유저(){
        // given
        String currentPassword = "Password123!";
        UserDeleteRequestDto userDeleteRequest = new UserDeleteRequestDto(currentPassword);

        // Mocking repository to return empty
        when(userRepository.findById(10l)).thenReturn(Optional.empty());

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.delete(10l, userDeleteRequest) // Code that triggers the exception
        );

        // Verify
        assertEquals("404 NOT_FOUND 존재하지 않는 유저 입니다.", exception.getMessage());
    }

    @Test
    void User_삭제_실패_틀린_비밀번호(){
        // given
        String wrongPassword = "Password123!";
        UserDeleteRequestDto userDeleteRequest = new UserDeleteRequestDto(wrongPassword);

        // Mocking repository to return empty
        when(userRepository.findById(10l)).thenReturn(Optional.of(user));
        when(user.getStatus()).thenReturn(UserStatus.ACTIVATED);
        when(passwordEncoder.matches(userDeleteRequest.getPassword(), user.getPassword())).thenReturn(false);

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.delete(10l, userDeleteRequest) // Code that triggers the exception
        );

        // Verify
        assertEquals("400 BAD_REQUEST 비밀 번호가 아이디와 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    void User_조회_성공() {
        // Given
        Long userId = 1L;
        String encodedPassword = "encodedPassword123!";
        User user = new User(signUpRequest, encodedPassword);

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserSearchResponseDto response = userService.find(userId);

        // Then
        assertNotNull(response);
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getName(), response.getName());
    }

    @Test
    void User_조회_실패_없는_유저() {
        // Mocking repository to return empty
        when(userRepository.findById(10l)).thenReturn(Optional.empty());

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.find(10l) // Code that triggers the exception
        );

        // Verify
        assertEquals("404 NOT_FOUND 존재하지 않는 유저 입니다.", exception.getMessage());
    }

    @Test
    void User_조회_실패_삭제된_유저() {
        // Mocking repository to return a user
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        when(user.getStatus()).thenReturn(UserStatus.DELETED);

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.find(1l) // Code that triggers the exception
        );

        // Verify
        assertEquals("404 NOT_FOUND 존재하지 않는 유저 입니다.", exception.getMessage());
    }


    // checkUser
    @Test
    void User_확인_성공() {
        // when
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.checkUser(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void User_확인_실패_없는_유저() {
        // when
        when(userRepository.findById(10l)).thenReturn(Optional.empty());

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.checkUser(10l) // Code that triggers the exception
        );

        // Verify
        assertEquals("404 NOT_FOUND 존재하지 않는 유저 입니다.", exception.getMessage());
    }

    @Test
    void User_확인_실패_삭제된_유저() {
        // Mocking repository to return a user
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        when(user.getStatus()).thenReturn(UserStatus.DELETED);

        // When & Then
        HotSixException exception = assertThrows(
                HotSixException.class, // Exception class
                () -> userService.checkUser(1l) // Code that triggers the exception
        );

        // Verify
        assertEquals("404 NOT_FOUND 존재하지 않는 유저 입니다.", exception.getMessage());
    }
}



