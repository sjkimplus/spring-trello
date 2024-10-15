package com.sparta.springtrello.domain.workspace.service;

import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.user.service.UserService;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceEditRequestDto;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceSaveRequestDto;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceEditResponseDto;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceSaveResponseDto;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                new IllegalArgumentException("User not found"));

        // 어드민 유저만 워크스페이스 생성가능
        if (!user.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalArgumentException("User is not admin");
        }


        Workspace workspace = workspaceRepository.save(new Workspace(workspaceSaveRequestDto));

        return new WorkspaceSaveResponseDto(workspace);
    }

//    @Transactional(readOnly = true)
//    public List<WorkspaceReadResponseDto> readWorkspace(AuthUser authUser) {
//
//
//        // 유저검증
//        Long userId = authUser.getId();
//        userService.checkUser(userId);
//
//
//        List<Workspace> workspaces = workspaceRepository.findAllByMemberId();
//
//        return workspaces.stream()
//                .map(workspace -> new WorkspaceReadResponseDto(
//                        workspace.getId(),
//                        workspace.getTitle(),
//                        workspace.getTitle(),
//                        workspace.getCreatedAt(),
//                        workspace.getModifiedAt()
//                )).toList();
//    }

    public WorkspaceEditResponseDto editWorkspace(AuthUser authUser,
                                                  WorkspaceEditRequestDto workspaceEditRequestDto,
                                                  Long id) {

        // 유저검증
        Long userId = authUser.getId();
        userService.checkUser(userId);

//        Member member = memberRepository.findByUserId(userId).orElseThrow(() ->
//                new IllegalArgumentException("User not found"));
//
//        // 수정, 삭제 불가(읽기 권한일 경우)
//        if (member.getRole().equals(MemberRole.Reader)) {
//            throw new IllegalArgumentException("You cannot edit a read workspace");
//        }

        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("워크스페이스 없음"));

        workspace.update(workspaceEditRequestDto);
        workspaceRepository.save(workspace);

        return new WorkspaceEditResponseDto(workspace);
    }

    public void deleteWorkspace(Long id, AuthUser authUser) {

        // 유저검증
        Long userId = authUser.getId();
        userService.checkUser(userId);

//        Member member = memberRepository.findByUserId(userId).orElseThrow(() ->
//                new IllegalArgumentException("User not found"));
//
//        // 수정, 삭제 불가(읽기 권한일 경우)
//        if (member.getRole().equals(MemberRole.Reader)) {
//            throw new IllegalArgumentException("You cannot edit a read workspace");
//        }

        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("워크스페이스 없음"));

        workspaceRepository.deleteById(id);
    }
}
