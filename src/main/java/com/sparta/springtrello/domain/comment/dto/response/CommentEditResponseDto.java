package com.sparta.springtrello.domain.comment.dto.response;

import com.sparta.springtrello.domain.comment.entity.Comment;

public class CommentEditResponseDto {

    private Long id;
    private String content;

    public CommentEditResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
    }
}
