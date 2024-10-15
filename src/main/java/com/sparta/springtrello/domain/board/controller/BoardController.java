package com.sparta.springtrello.domain.board.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
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

    @PostMapping
    public ResponseEntity<ApiResponseDto<BoardResponseDto>> createBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                        @RequestParam Long workspaceId,
                                                                        @RequestParam String title,
                                                                        @RequestParam(required = false) String background,
                                                                        @RequestParam(required = false) String image) {
        return ResponseEntity.ok(ApiResponseDto.success(boardService.createBoard(authUser, workspaceId, title, background, image)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<BoardResponseDto>> getBoard(@PathVariable Long id)
    {
        return ResponseEntity.ok(ApiResponseDto.success(boardService.getBoard(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<BoardResponseDto>> updateBoard(@PathVariable Long id,
                                         @AuthenticationPrincipal AuthUser authUser,
                                         @RequestParam String title,
                                         @RequestParam(required = false) String background,
                                         @RequestParam(required = false) String image){
        return ResponseEntity.ok(ApiResponseDto.success(boardService.updateBoard(id, authUser, title, background, image)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteBoard(@PathVariable Long id,
                                         @AuthenticationPrincipal AuthUser authUser) {
        boardService.deleteBoard(id, authUser);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

}
