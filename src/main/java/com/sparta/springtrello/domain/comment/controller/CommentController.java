package com.sparta.springtrello.domain.comment.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.domain.comment.dto.request.CommentEditRequestDto;
import com.sparta.springtrello.domain.comment.dto.request.CommentSaveRequestDto;
import com.sparta.springtrello.domain.comment.dto.response.CommentEditResponseDto;
import com.sparta.springtrello.domain.comment.dto.response.CommentSaveResponseDto;
import com.sparta.springtrello.domain.comment.service.CommentService;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<ApiResponseDto<CommentSaveResponseDto>> saveComment(@AuthenticationPrincipal AuthUser authUser,
                                                                              @RequestParam Long cardId,
                                                                              @RequestBody CommentSaveRequestDto commentSaveRequestDto) {
        CommentSaveResponseDto saveResponseDto = commentService.saveComment(authUser, cardId, commentSaveRequestDto);
        return ResponseEntity.ok(ApiResponseDto.success(saveResponseDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CommentEditResponseDto>> editComment(@AuthenticationPrincipal AuthUser authUser,
                                                                              @PathVariable Long id,
                                                                              @RequestBody CommentEditRequestDto commentEditRequestDto) {
        CommentEditResponseDto editResponseDto = commentService.editComment(authUser, id, commentEditRequestDto);
        return ResponseEntity.ok(ApiResponseDto.success(editResponseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteComment(@PathVariable Long id,
                                                              @AuthenticationPrincipal AuthUser authUser) {
        commentService.deleteComment(id, authUser);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }
}
