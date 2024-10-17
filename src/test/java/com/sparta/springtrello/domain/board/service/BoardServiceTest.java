package com.sparta.springtrello.domain.board.service;

import com.sparta.springtrello.common.Status;
import com.sparta.springtrello.domain.board.dto.BoardResponseDto;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.kanban.entity.Kanban;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.entity.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    void 보드생성_성공() {

        AuthUser authUser = new AuthUser(1L, "email@email.com", UserRole.ROLE_ADMIN);
        Long workspaceId = 1L;
        String title = "title";
        String background = "background";
        String image = "image";

        User user = new User(1L, "email@email.com", UserRole.ROLE_ADMIN);
        Workspace workspace = new Workspace(1L, "title", "content", null);
        Member member = new Member(user, workspace, MemberRole.ROLE_WORKSPACE);

        given(memberRepository.findByWorkspaceIdAndUserId(workspaceId,authUser.getId())).willReturn(Optional.of(member));
        given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(workspace));

        BoardResponseDto boardResponseDto = boardService.createBoard(authUser, workspaceId, title, background, image);

        assertNotNull(boardResponseDto);
        then(boardRepository).should().save(any(Board.class));

    }

    @Test
    void 보드수정_성공() {

        AuthUser authUser = new AuthUser(1L, "email@email.com", UserRole.ROLE_ADMIN);
        Long id = 1L;
        String title = "Utitle";
        String background = "Ubackground";
        String image = "Uimage";

        User user = new User(1L, "email@email.com", UserRole.ROLE_ADMIN);
        Workspace workspace = new Workspace(1L, "title", "content", null);
        Member member = new Member(user, workspace, MemberRole.ROLE_WORKSPACE);
        ReflectionTestUtils.setField(member, "id", 1L);
        Board board = new Board(member, workspace, "title", "background", "image");
        ReflectionTestUtils.setField(board, "id", 1L);
        Kanban kanban = new Kanban(1, "kanbantitle", board);
        ReflectionTestUtils.setField(kanban, "id", 1L);

        Long workspaceId = workspace.getId();

        given(boardRepository.findById(id)).willReturn(Optional.of(board));
        given(memberRepository.findByWorkspaceIdAndUserId(workspaceId,authUser.getId())).willReturn(Optional.of(member));

        BoardResponseDto boardResponseDto = boardService.updateBoard(id, authUser, title, background, image);

        assertNotNull(boardResponseDto);
        assertEquals(title, boardResponseDto.getTitle());
        assertEquals(background, boardResponseDto.getBackground());
        assertEquals(image, boardResponseDto.getImage());
    }

    @Test
    void 보드삭제_성공() {
        Long id = 1L;
        AuthUser authUser = new AuthUser(1L, "email@email.com", UserRole.ROLE_ADMIN);

        User user = new User(1L, "email@email.com", UserRole.ROLE_ADMIN);
        Workspace workspace = new Workspace(1L, "title", "content", null);
        Member member = new Member(user, workspace, MemberRole.ROLE_WORKSPACE);
        ReflectionTestUtils.setField(member, "id", 1L);
        Board board = new Board(member, workspace, "title", "background", "image");
        ReflectionTestUtils.setField(board, "id", 1L);
        Kanban kanban = new Kanban(1, "kanbantitle", board);
        ReflectionTestUtils.setField(kanban, "id", 1L);

        Long workspaceId = workspace.getId();

        given(boardRepository.findById(id)).willReturn(Optional.of(board));
        given(memberRepository.findByWorkspaceIdAndUserId(workspaceId,authUser.getId())).willReturn(Optional.of(member));

        boardService.deleteBoard(id, authUser);

        assertEquals(board.getStatus(), Status.DELETED);
    }
}