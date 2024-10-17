package com.sparta.springtrello.domain.kanban.service;

import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.kanban.dto.request.KanbanRequestDto;
import com.sparta.springtrello.domain.kanban.dto.response.KanbanResponseDto;
import com.sparta.springtrello.domain.kanban.entity.Kanban;
import com.sparta.springtrello.domain.kanban.repository.KanbanRepository;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.entity.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.service.UserService;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class KanbanServiceTest {

    @Mock
    private KanbanRepository kanbanRepository;

    @Mock
    private UserService userService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private KanbanService kanbanService;

    @Test
    public void 칸반_생성_성공() {
        //given
        long boardId = 1L;
        AuthUser authUser = new AuthUser(1L, "a", UserRole.ROLE_ADMIN);
        KanbanRequestDto kanbanRequestDto = new KanbanRequestDto("a");
        User user = new User(authUser.getId(), authUser.getEmail(), UserRole.ROLE_ADMIN);
        Workspace workspace = new Workspace(1L, "title", "content", null);
        Member member = new Member(user, workspace, MemberRole.ROLE_WORKSPACE);
        Board board = new Board(member, workspace, "a", "a", "a");

        // Mocking behavior
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(memberRepository.findByWorkspaceIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(member));

        given(kanbanRepository.findMaxOrderByBoard(any())).willReturn(null);

        Kanban kanban = new Kanban(1,"a",board);
        //When
        kanbanService.createKanban(authUser,boardId,kanbanRequestDto);
        //Then
        assertNotNull(kanban);
        assertEquals(kanban.getTitle(), kanbanRequestDto.getTitle());
    }
}
