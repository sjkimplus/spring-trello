package com.sparta.springtrello.domain.comment.dto.response;

import com.sparta.springtrello.domain.comment.entity.Comment;

public class CommentSaveResponseDto {

    private Long id;
    private String content;

    public CommentSaveResponseDto(Comment comment) {

        this.id = comment.getId();
        this.content = comment.getContent();

    }
}
