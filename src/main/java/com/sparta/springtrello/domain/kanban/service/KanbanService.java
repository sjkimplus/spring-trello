package com.sparta.springtrello.domain.kanban.service;

import com.sparta.springtrello.common.exception.ErrorCode;
import com.sparta.springtrello.common.exception.HotSixException;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.kanban.dto.request.KanbanSaveRequestDto;
import com.sparta.springtrello.domain.kanban.entity.Kanban;
import com.sparta.springtrello.domain.kanban.repository.KanbanRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KanbanService {

    private final KanbanRepository kanbanRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public void createKanban(AuthUser authUser, Long id, KanbanSaveRequestDto requestDto) {
        //유저 확인
        userService.checkUser(authUser.getId());
        //해당 보드 있는지 확인
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> new HotSixException(ErrorCode.BOARD_NOT_FOUND));
        //칸반 순서를 위한 order 자동 생성
        Integer maxOrder = kanbanRepository.findMaxOrderByBoard(board);
        Integer newOrder = maxOrder ==null? 1: maxOrder +1;
        //칸반 생성 후 레파지토리에 저장
        Kanban kanban = new Kanban(newOrder,requestDto.getTitle(),board,requestDto.getKanbanStatus());
        kanbanRepository.save(kanban);
    }

    @Transactional
    public void updateKanban(AuthUser authUser, Long id,Long kanbansId, KanbanSaveRequestDto requestDto) {
        userService.checkUser(authUser.getId());
        //해당 보드 있는지 확인
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> new HotSixException(ErrorCode.BOARD_NOT_FOUND));
        //해당 칸반 있는지 확인
        Kanban kanban = kanbanRepository.findById(kanbansId)
                .orElseThrow(() -> new HotSixException(ErrorCode.KANBAN_NOT_FOUND));
        //칸반 변경된 제목 업데이트
        kanban.updateKanban(requestDto.getTitle(),board,requestDto.getKanbanStatus());
    }

    @Transactional
    public void updateOrder(AuthUser authUser, Long id, Long kanbansId, Integer newOrder) {
        userService.checkUser(authUser.getId());
        //해당 보드 있는지 확인
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> new HotSixException(ErrorCode.BOARD_NOT_FOUND));
        //해당 칸반 있는지 확인
        Kanban kanban = kanbanRepository.findById(kanbansId)
                .orElseThrow(() -> new HotSixException(ErrorCode.KANBAN_NOT_FOUND));
        //기존 순서 가져오기
        Integer oldOrder = kanban.getKanbanOrder();
        if(newOrder<oldOrder){
            kanbanRepository.decreaseOrderBetween(board,newOrder,oldOrder-1);
        }else if(newOrder > oldOrder){
            kanbanRepository.increaseOrderBetween(board,oldOrder+1,newOrder);
        }else{
            throw new HotSixException(ErrorCode.KANBAN_SAME_ORDER);
        }
        kanban.updateOrder(newOrder);
        kanbanRepository.save(kanban);
    }

    @Transactional
    public void deleteKanban(AuthUser authUser, Long id, Long kanbansId, KanbanSaveRequestDto requestDto) {
        userService.checkUser(authUser.getId());
        //해당 보드 있는지 확인
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> new HotSixException(ErrorCode.BOARD_NOT_FOUND));
        //해당 칸반 있는지 확인
        Kanban kanban = kanbanRepository.findById(kanbansId)
                .orElseThrow(() -> new HotSixException(ErrorCode.KANBAN_NOT_FOUND));
        //칸반 변경된 상태 업데이트
        kanban.deleteKanban(requestDto.getKanbanStatus());
    }

    public List<Kanban> getKanbans(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> new HotSixException(ErrorCode.BOARD_NOT_FOUND));
        return kanbanRepository.findByBoard(board);
    }
}
