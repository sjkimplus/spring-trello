package com.sparta.springtrello.domain.board.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.domain.board.dto.BoardDetailResponseDto;
import com.sparta.springtrello.domain.board.dto.BoardResponseDto;
import com.sparta.springtrello.domain.board.service.BoardService;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    /**
     * Board 생성
     * @param authUser : 로그인 사용자 정보가 담긴 객체
     * @param workspaceId : Board가 등록되는 WorkSpace의 Id
     * @param title : Board의 제목
     * @param background : Board의 배경색
     * @param image : Board의 배경이미지
     * @return : 생성된 Board 정보
     */

    @PostMapping
    public ResponseEntity<ApiResponseDto<BoardResponseDto>> createBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                        @RequestParam Long workspaceId,
                                                                        @RequestParam String title,
                                                                        @RequestParam(required = false) String background,
                                                                        @RequestParam(required = false) String image) {
        return ResponseEntity.ok(ApiResponseDto.success(boardService.createBoard(authUser, workspaceId, title, background, image)));
    }

    /**
     * Board 단건조회
     * @param id : 조회하는 Board의 Id
     * @return : Board 및 Board가 가지고있는 Kanban,Ticket 게시
     */

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<BoardDetailResponseDto>> getBoard(@PathVariable Long id)
    {
        return ResponseEntity.ok(ApiResponseDto.success(boardService.getBoard(id)));
    }

    /**
     * Board 수정
     * @param id : Board의 Id값
     * @param authUser : 사용자 정보가 담긴 객체
     * @param title : 수정할 Board의 제목
     * @param background : 수정할 Board의 배경색
     * @param image : 수정할 Board의 배경이미지
     * @return : 수정된 Board의 정보
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<BoardResponseDto>> updateBoard(@PathVariable Long id,
                                         @AuthenticationPrincipal AuthUser authUser,
                                         @RequestParam String title,
                                         @RequestParam(required = false) String background,
                                         @RequestParam(required = false) String image){
        return ResponseEntity.ok(ApiResponseDto.success(boardService.updateBoard(id, authUser, title, background, image)));
    }

    /**
     * Board의 soft-delete
     * @param id : 삭제하는 Board의 Id
     * @param authUser : 사용자 정보가 담긴 객체
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteBoard(@PathVariable Long id,
                                         @AuthenticationPrincipal AuthUser authUser) {
        boardService.deleteBoard(id, authUser);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

}
