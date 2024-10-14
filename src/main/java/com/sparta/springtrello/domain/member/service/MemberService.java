package com.sparta.springtrello.domain.member.service;

import com.sparta.springtrello.common.exception.ErrorCode;
import com.sparta.springtrello.common.exception.HotSixException;
import com.sparta.springtrello.domain.member.dto.request.MemberSaveRequestDto;
import com.sparta.springtrello.domain.member.dto.response.MemberResponseDto;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.dto.response.UserResponse;
import com.sparta.springtrello.domain.user.entity.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveMember(AuthUser authUser,long id,
                           long userId,
                           MemberSaveRequestDto requestDto) {
        //입력 받은 userid 와 workspaceId 가 유효한 값인지 확인 및 생성
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new HotSixException(ErrorCode.USER_NOT_FOUND));
        Workspace workspace = workspaceRepository
                .findById(id).orElseThrow(()-> new HotSixException(ErrorCode.WORKSPACE_NOT_FOUND));
        //이미 매니저 등록되어있는지 확인 후 에러 발생
        if(memberRepository.existsByWorkspaceAndUser(workspace,user)){
            throw new HotSixException(ErrorCode.MEMBER_RESIST_DUPLICATION);
        }
        //멤버로 등록
        Member member = new Member(user,workspace,requestDto.getMemberRole());
        memberRepository.save(member);
    }


    public List<MemberResponseDto> getMembers(long id) {
        //불러올 워크페이스 존재 여부 확인
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new HotSixException(ErrorCode.WORKSPACE_NOT_FOUND));

        //멤버 리스트 만들기
        return memberRepository.findByWorkspaceId(id).stream()
                .map(member -> new MemberResponseDto(
                        member.getId(),
                        new UserResponse(member.getUser().getId(), member.getUser().getUsername())
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMember(AuthUser authUser,Long id, Long memberId, MemberSaveRequestDto requestDto) {
//        User user = userRepository.findById(authUser.getUserId).orElseThrow(()-> new NullPointerException("admin이 아닙니다."));
        //불러올 워크페이스 존재 여부 확인
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(()-> new HotSixException(ErrorCode.WORKSPACE_NOT_FOUND));

        //해당 워크스페이스에 해당 멤버가 있는지 확인
        Member member = memberRepository.findByWorkspaceAndId(workspace, memberId)
                .orElseThrow(() -> new HotSixException(ErrorCode.MEMBER_NOT_FOUND));

        member.deleteMember(requestDto.getMemberRole());
   }
}
