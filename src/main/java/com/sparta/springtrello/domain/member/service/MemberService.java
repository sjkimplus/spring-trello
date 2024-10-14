package com.sparta.springtrello.domain.member.service;

import com.sparta.springtrello.domain.member.dto.request.MemberSaveRequestDto;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspace;
    private final UserRepository userRepository;

    public Optional saveMember(long id,
                               long userId,
                               MemberSaveRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NullPointerException("User not found"));
        Workspace workspace =

    }
}
