package com.sparta.springtrello.domain.workspace.service;

import com.sparta.springtrello.common.exception.ErrorCode;
import com.sparta.springtrello.common.exception.HotSixException;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.entity.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.user.service.UserService;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceEditRequestDto;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceSaveRequestDto;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceEditResponseDto;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceReadResponseDto;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceSaveResponseDto;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final UserService userService;

    public WorkspaceSaveResponseDto createWorkspace(AuthUser authUser,
                                                    WorkspaceSaveRequestDto workspaceSaveRequestDto) {

        // 유저검증
        Long id = authUser.getId();
        User user = userRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.USER_NOT_FOUND));

        // 어드민 유저만 워크스페이스 생성가능
        if (!user.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new HotSixException(ErrorCode.USER_NO_AUTHORITY);
        }

        Workspace workspace = workspaceRepository.save(new Workspace(workspaceSaveRequestDto));

        return new WorkspaceSaveResponseDto(workspace);
    }

    @Transactional(readOnly = true)
    public List<WorkspaceReadResponseDto> readWorkspace(AuthUser authUser) {

        // 유저검증
        Long userId = authUser.getId();
        userService.checkUser(userId);

        List<Workspace> workspaces = workspaceRepository.findAllByMemberId(userId);

        return workspaces.stream()
                .map(workspace -> new WorkspaceReadResponseDto(
                        workspace.getId(),
                        workspace.getTitle(),
                        workspace.getContent(),
                        workspace.getCreatedAt(),
                        workspace.getModifiedAt()
                )).toList();
    }

    public WorkspaceEditResponseDto editWorkspace(AuthUser authUser,
                                                  WorkspaceEditRequestDto workspaceEditRequestDto,
                                                  Long id) {

        // 유저검증
        Long userId = authUser.getId();
        userService.checkUser(userId);

        Member member = memberRepository.findByUserId(userId).orElseThrow(() ->
                new HotSixException(ErrorCode.USER_NO_AUTHORITY));

        Workspace workspace = workspaceRepository.findById(id).orElseThrow(()->
                new HotSixException(ErrorCode.WORKSPACE_NOT_FOUND));

        // Member가 해당 워크스페이스에 속해 있는지 확인
        if (!workspace.getMembers().contains(member)) {
            throw new HotSixException(ErrorCode.USER_NO_AUTHORITY);
        }

        // 수정, 삭제 불가(읽기 권한일 경우)
        if (!member.getMemberRole().equals(MemberRole.CREATOR)) {
            throw new HotSixException(ErrorCode.USER_NO_AUTHORITY);
        }

        workspace.update(workspaceEditRequestDto);
        workspaceRepository.save(workspace);

        return new WorkspaceEditResponseDto(workspace);
    }

    public void deleteWorkspace(Long id, AuthUser authUser) {

        // 유저검증
        Long userId = authUser.getId();
        userService.checkUser(userId);

        Member member = memberRepository.findByUserId(userId).orElseThrow(() ->
                new HotSixException(ErrorCode.USER_NO_AUTHORITY));

        // 수정, 삭제 불가(읽기 권한일 경우)
        if (!member.getMemberRole().equals(MemberRole.CREATOR)) {
            throw new HotSixException(ErrorCode.USER_NO_AUTHORITY);
        }

        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.WORKSPACE_NOT_FOUND));

        workspaceRepository.deleteById(id);
    }
}
