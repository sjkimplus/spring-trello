package com.sparta.springtrello.domain.board.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ApiResponseDto<?> createBoard(@AuthenticationPrincipal SignUser signUser,
                                         @RequestParam Long workspaceId,
                                         @RequestParam String title,
                                         @RequestParam(required = false) String background,
                                         @RequestParam(required = false) String image) {
        return new ApiResponseDto<>(boardService.createBoard(signUser, workspaceId, title, background, image));
    }

    @GetMapping("/{id}")
    public ApiResponseDto<?> getBoard(@PathVariable Long id)
    {
        return new ApiResponseDto<>(boardService.getBoard(id));
    }

    @PutMapping("/{id}")
    public ApiResponseDto<?> updateBoard(@PathVariable Long id,
                                         @AuthenticationPrincipal SignUser signUser,
                                         @RequestParam String title,
                                         @RequestParam(required = false) String background,
                                         @RequestParam(required = false) String image){
        return new ApiResponseDto<>(boardService.updateBoard(id, signUser, title, background, image));
    }

    @DeleteMapping("/{id}")
    public ApiResponseDto<?> deleteBoard(@PathVariable Long id,
                                         @AuthenticationPrincipal SignUser signUser) {
        return new ApiResponseDto<>(ApiResponseDto.Status.SUCCESS, boardService.deleteBoard(id, signUser), "dd");
    }

}
