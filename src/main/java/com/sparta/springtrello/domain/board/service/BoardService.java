package com.sparta.springtrello.domain.board.service;

import com.sparta.springtrello.domain.board.dto.BoardResponseDto;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BoardResponseDto createBoard(AuthUser authUser, Long workspaceId, String title, String background, String image) {

        //Board를 등록하는 멤버 찾기
        Member member = memberRepository.findById(authUser.getId()).orElseThrow();

        //Board를 등록하려는 유저의 role이 createor인지 확인
        if (!member.getMemberRole().toString().equals("CREATOR")) throw new RuntimeException();

        //Board를 등록할 워크스페이스 찾기
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow();

        //Board 생성
        Board board = new Board(member.getUser(), workspace, title, background, image);

        //Board DB에 저장
        boardRepository.save(board);

        //ResponseDto 반환
        return new BoardResponseDto(
                board.getUser().getId(),
                workspaceId,
                title,
                background,
                image
        );
    }

    public BoardResponseDto getBoard(Long id) {
        //Board 찾기
        Board board = boardRepository.findById(id).orElseThrow();

        //ResponseDto 반환
        return new BoardResponseDto(
                board.getUser().getId(),
                board.getWorkspace().getId(),
                board.getTitle(),
                board.getBackground(),
                board.getImage()
        );
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, AuthUser authUser, String title, String background, String image) {
        //Board를 등록하는 멤버 찾기
        Member member = memberRepository.findById(authUser.getId()).orElseThrow();

        //Board를 등록하려는 유저의 role이 createor인지 확인
        if (!member.getMemberRole().toString().equals("CREATOR")) throw new RuntimeException();

        //수정하려는 Board 찾기
        Board board = boardRepository.findById(id).orElseThrow();

        //Board 수정
        board.updateBoard(title, background, image);

        //ResponseDto 반환
        return new BoardResponseDto(
                board.getUser().getId(),
                board.getWorkspace().getId(),
                board.getTitle(),
                board.getBackground(),
                board.getImage()
        );
    }

    @Transactional
    public void deleteBoard(Long id, AuthUser authUser) {

        //Board를 등록하는 멤버 찾기
        Member member = memberRepository.findById(authUser.getId()).orElseThrow();

        //Board를 등록하려는 유저의 role이 createor인지 확인
        if (!member.getMemberRole().toString().equals("CREATOR")) throw new RuntimeException();

        //삭제하려는 Board 찾기
        Board board = boardRepository.findById(id).orElseThrow();

        //Board 수정
        board.deleteBoard();

        //Board 하위계층인 kanban, ticket, comment의 soft-delete 연동

    }
}
