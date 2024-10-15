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
import com.sparta.springtrello.domain.user.enums.UserStatus;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.springtrello.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public SignUpResponseDto create(SignUpRequestDto requestDto) {
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) throw new HotSixException(USER_ID_DUPLICATION);

        // 유저 생성
        User user = new User(requestDto, password);
        userRepository.save(user);

        return new SignUpResponseDto(requestDto);
    }

    public SignInResponseDto login(JwtUtil jwtUtil, SignInRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new HotSixException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new HotSixException(USER_PW_ERROR);
        }

        if (user.getStatus()==UserStatus.DELETED)
            throw new HotSixException(USER_NOT_FOUND);

        String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());
        return new SignInResponseDto(user, token);
    }

    @Transactional
    public String delete(Long id, UserDeleteRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new HotSixException(USER_NOT_FOUND)
        );

        if (user.getStatus()== UserStatus.DELETED)
            throw new HotSixException(USER_NOT_FOUND);

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new HotSixException(USER_PW_ERROR);
        }

        user.deactivateUser();

        return "삭제 완료";
    }

    public UserSearchResponseDto find(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new HotSixException(USER_NOT_FOUND)
        );

        if (user.getStatus()==UserStatus.DELETED)
            throw new HotSixException(USER_NOT_FOUND);

        return new UserSearchResponseDto(user);
    }

    // 유저검증용 메서드
    public void checkUser (Long id) {

        User user = userRepository.findById(id).orElseThrow(() ->
                new HotSixException(USER_NOT_FOUND)
        );

        if (user.getStatus()==UserStatus.DELETED)
            throw new HotSixException(USER_NOT_FOUND);

    }
}

