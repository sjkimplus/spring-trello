package com.sparta.springtrello.domain.comment.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.domain.comment.dto.request.CommentEditRequestDto;
import com.sparta.springtrello.domain.comment.dto.request.CommentSaveRequestDto;
import com.sparta.springtrello.domain.comment.dto.response.CommentEditResponseDto;
import com.sparta.springtrello.domain.comment.dto.response.CommentSaveResponseDto;
import com.sparta.springtrello.domain.comment.service.CommentService;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.slack.SlackNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final SlackNotificationService slackNotificationService;

    @PostMapping()
    public ResponseEntity<ApiResponseDto<CommentSaveResponseDto>> saveComment(@AuthenticationPrincipal AuthUser authUser,
                                                                              @RequestParam Long cardId,
                                                                              @RequestBody CommentSaveRequestDto commentSaveRequestDto) {
        CommentSaveResponseDto saveResponseDto = commentService.saveComment(authUser, cardId, commentSaveRequestDto);
        String slackMessage = String.format("Ticket ID: %d - 새로운 댓글이 등록되었습니다", cardId);
        slackNotificationService.sendSlackMessage(slackMessage);
        return ResponseEntity.ok(ApiResponseDto.success(saveResponseDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CommentEditResponseDto>> editComment(@AuthenticationPrincipal AuthUser authUser,
                                                                              @PathVariable Long id,
                                                                              @RequestParam Long cardId,
                                                                              @RequestBody CommentEditRequestDto commentEditRequestDto) {
        CommentEditResponseDto editResponseDto = commentService.editComment(authUser, id, cardId, commentEditRequestDto);
        return ResponseEntity.ok(ApiResponseDto.success(editResponseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteComment(@PathVariable Long id,
                                                              @RequestParam Long cardId,
                                                              @AuthenticationPrincipal AuthUser authUser) {
        commentService.deleteComment(id, cardId,authUser);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }
}
