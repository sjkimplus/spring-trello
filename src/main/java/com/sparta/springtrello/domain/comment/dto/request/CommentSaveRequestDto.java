package com.sparta.springtrello.domain.comment.dto.request;

import com.sparta.springtrello.domain.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentSaveRequestDto {

    private String content;

}
