package com.sparta.springtrello.domain.board.service;

import com.sparta.springtrello.common.exception.ErrorCode;
import com.sparta.springtrello.common.exception.HotSixException;
import com.sparta.springtrello.domain.board.dto.BoardDetailResponseDto;
import com.sparta.springtrello.domain.board.dto.BoardResponseDto;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.kanban.dto.response.KanbanResponseDto;
import com.sparta.springtrello.domain.kanban.entity.Kanban;
import com.sparta.springtrello.domain.kanban.repository.KanbanRepository;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.entity.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import com.sparta.springtrello.domain.ticket.repository.TicketRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.service.UserService;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;
    private final KanbanRepository kanbanRepository;
    private final TicketRepository ticketRepository;
    private final UserService userService;

    @Transactional
    public BoardResponseDto createBoard(AuthUser authUser, Long workspaceId, String title, String background, String image) {

        //유저의 멤버 정보 가져오기
        Member member = memberRepository.findByWorkspaceIdAndUserId(workspaceId,authUser.getId())
                .orElseThrow(()-> new HotSixException(ErrorCode.USER_NOT_FOUND));

        //Board를 등록하려는 유저의 role이 reader인지 확인
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)){
            throw new RuntimeException();
        }

        //Board를 등록할 워크스페이스 찾기
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() ->
                new HotSixException(ErrorCode.WORKSPACE_NOT_FOUND));

        //Board 생성
        Board board = new Board(member, workspace, title, background, image);

        //Board DB에 저장
        boardRepository.save(board);

        //ResponseDto 반환
        return new BoardResponseDto(board);
    }

    public BoardDetailResponseDto getBoard(Long id) {
        //Board 찾기
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.BOARD_NOT_FOUND));

        //Board에 속한 kanban 전체찾기
        List<Kanban> kanbanList = kanbanRepository.findAllByBoard_Id(board.getId());

        List<KanbanResponseDto> kanbanResponseDtos = new ArrayList<>();
        for(Kanban kanban : kanbanList)
        {
            //Kanban에 속한 Ticket들 찾기
            List<Ticket> ticketList = ticketRepository.findAllByKanban_id(kanban.getId());

            //Dto에 담기
            List<TicketResponseDto> ticketResponseDtos = ticketList.stream().map(TicketResponseDto::new).toList();
            kanbanResponseDtos.add(new KanbanResponseDto(kanban, ticketResponseDtos));
        }

        return new BoardDetailResponseDto(board, kanbanResponseDtos);
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, AuthUser authUser, String title, String background, String image) {

        //수정하려는 Board 찾기
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.BOARD_NOT_FOUND));

        //Board를 등록하는 멤버 찾기
        Member member = memberRepository.findByWorkspaceIdAndUserId(board.getWorkspace().getId(),authUser.getId())
                .orElseThrow(()-> new HotSixException(ErrorCode.USER_NOT_FOUND));

        //Board를 등록하려는 유저의 role이 reader인지 확인
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)){
            throw new RuntimeException();
        }

        //Board 수정
        board.updateBoard(title, background, image);

        //ResponseDto 반환
        return new BoardResponseDto(board);
    }

    @Transactional
    public void deleteBoard(Long id, AuthUser authUser) {

        //삭제하려는 Board 찾기
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.BOARD_NOT_FOUND));

        //Board를 등록하는 멤버 찾기
        Member member = memberRepository.findByWorkspaceIdAndUserId(board.getWorkspace().getId(),authUser.getId())
                .orElseThrow(()-> new HotSixException(ErrorCode.USER_NOT_FOUND));

        //Board를 등록하려는 유저의 role이 reader인지 확인
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)){
            throw new RuntimeException();
        }

        //Board soft-delete
        board.deleteBoard();

        //Board 하위계층인 kanban, ticket, comment의 soft-delete 연동

    }
}
