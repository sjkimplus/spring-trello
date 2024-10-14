package com.sparta.springtrello.domain.kanban.service;

import com.sparta.springtrello.common.exception.ErrorCode;
import com.sparta.springtrello.common.exception.HotSixException;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.kanban.dto.request.KanbanSaveRequestDto;
import com.sparta.springtrello.domain.kanban.entity.Kanban;
import com.sparta.springtrello.domain.kanban.repository.KanbanRepository;
import com.sparta.springtrello.domain.user.entity.AuthUser;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KanbanService {

    private final KanbanRepository kanbanRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createKanban(AuthUser authUser, Long id, KanbanSaveRequestDto requestDto) {
//        User user = userRepository.findById(authUser.) authUser로 userRole 확인하기
        //해당 보드 있는지 확인
        Board board = boardRepository.findById(id).orElseThrow(()-> new HotSixException(ErrorCode.BOARD_NOT_FOUND));
        //칸반 생성 후 레파지토리에 저장
        Kanban kanban = new Kanban(requestDto.getTitle(),board,requestDto.getKanbanStatus());
        kanbanRepository.save(kanban);
    }

    @Transactional
    public void updateKanban(AuthUser authUser, Long id,Long kanbansId, KanbanSaveRequestDto requestDto) {
    //        User user = userRepository.findById(authUser.) authUser로 userRole 확인하기
        //해당 보드 있는지 확인
        Board board = boardRepository.findById(id).orElseThrow(()-> new HotSixException(ErrorCode.BOARD_NOT_FOUND));
        //해당 칸반 있는지 확인
        Kanban kanban = kanbanRepository.findById(kanbansId).orElseThrow(() -> new HotSixException(ErrorCode.KANBAN_NOT_FOUND));
        //칸반 변경된 제목 업데이트
        kanban.updateKanban(requestDto.getTitle(),board,requestDto.getKanbanStatus());
    }

    @Transactional
    public void deleteKanban(AuthUser authUser, Long id, Long kanbansId, KanbanSaveRequestDto requestDto) {
        //        User user = userRepository.findById(authUser.) authUser로 userRole 확인하기
        //해당 보드 있는지 확인
        Board board = boardRepository.findById(id).orElseThrow(()-> new HotSixException(ErrorCode.BOARD_NOT_FOUND));
        //해당 칸반 있는지 확인
        Kanban kanban = kanbanRepository.findById(kanbansId).orElseThrow(() -> new HotSixException(ErrorCode.KANBAN_NOT_FOUND));
        //칸반 변경된 상태 업데이트
        kanban.deleteKanban(requestDto.getKanbanStatus());
    }
}
