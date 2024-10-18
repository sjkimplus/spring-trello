package com.sparta.springtrello.domain.workspace.service;

import com.sparta.springtrello.common.exception.HotSixException;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.entity.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.user.service.UserService;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceSaveRequestDto;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceReadResponseDto;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceSaveResponseDto;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private WorkspaceService workspaceService;

    private AuthUser authUser;
    private User adminUser;
    private User normalUser;
    private Workspace workspace;
    private Member member;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser(1L, "1111@gmail.com", UserRole.ROLE_ADMIN);

        adminUser = new User(1L, "1111@gmail.com", UserRole.ROLE_ADMIN);

        normalUser = new User(2L, "1214323@gmail.com", UserRole.ROLE_USER);

        workspace = new Workspace(1L, "title", "contents");

        member = new Member(adminUser, workspace, MemberRole.ROLE_WORKSPACE);
    }

    @Test
    void 워크스페이스_생성_성공() {
        // given
        WorkspaceSaveRequestDto workspaceSaveRequestDto = new WorkspaceSaveRequestDto("new workspace", "workspace content");
        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(adminUser));
        given(workspaceRepository.save(any(Workspace.class))).willReturn(workspace);
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when
        WorkspaceSaveResponseDto response = workspaceService.createWorkspace(authUser, workspaceSaveRequestDto);

        // then
        assertNotNull(response);
        assertEquals(workspace.getId(), response.getId());
    }

    @Test
    void 워크스페이스_생성_실패_권한없음() {
        // given
        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(normalUser));

        // when & then
        HotSixException exception = assertThrows(HotSixException.class, () -> {
            workspaceService.createWorkspace(authUser, new WorkspaceSaveRequestDto("new workspace", "content"));
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void 워크스페이스_조회_성공() {
        // given
        given(workspaceRepository.findAllByMemberId(authUser.getId())).willReturn(List.of(workspace));

        // when
        List<WorkspaceReadResponseDto> workspaces = workspaceService.readWorkspace(authUser);

        // then
        assertNotNull(workspaces);
        assertEquals(1, workspaces.size());
        assertEquals(workspace.getId(), workspaces.get(0).getId());
    }
}