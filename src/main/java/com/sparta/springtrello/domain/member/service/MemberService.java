package com.sparta.springtrello.domain.member.service;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public void saveMember(AuthUser authUser,long id,
                           long userId,
                           MemberSaveRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NullPointerException("User not found"));
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(()-> new NullPointerException("workspace not found"));

        if(memberRepository.existsByWorkspaceAndUser(workspace,user)){
            throw new RuntimeException("이미 매니저로 등록되어있습니다.");
        }

        Member member = new Member(user,workspace,requestDto.getMemberRole());
        memberRepository.save(member);
    }

    public List<MemberResponseDto> getMembers(long id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("WorkSpace not found"));

        List<Member> memberList = memberRepository.findByWorkspaceId(id);

        List<MemberResponseDto> dtoList = new ArrayList<>();
        for(Member member : memberList){
            User user = member.getUser();
            dtoList.add(new MemberResponseDto(member.getId(),
                    new UserResponse(user.getId(), user.getUsername())));

        }
        return dtoList;
    }

    public void deleteMember(AuthUser authUser,Long id, Long memberId, MemberSaveRequestDto requestDto) {
//        User user = userRepository.findById(authUser.getUserId).orElseThrow(()-> new NullPointerException("admin이 아닙니다."));

        Workspace workspace = workspaceRepository.findById(id).orElseThrow(()-> new NullPointerException("workspace not found"));

        if(memberRepository.existsByWorkspaceAndMember(workspace,memberId)){
            throw new RuntimeException("해당 매니저가 존재하지 않습니다.");
        }

        Member member = memberRepository.findById(memberId).orElseThrow(()-> new NullPointerException("member not found"));

        member.deleteMember(requestDto.getMemberRole());
   }
}
