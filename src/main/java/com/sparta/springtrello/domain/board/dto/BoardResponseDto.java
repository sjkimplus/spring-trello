package com.sparta.springtrello.domain.board.dto;

import lombok.Getter;

@Getter
public class BoardResponseDto {

    private Long userId;
    private Long workspaceId;
    private String title;
    private String background;
    private String image;

    public BoardResponseDto(Long userId, Long workspaceId, String title, String background, String image) {
        this.userId = userId;
        this.workspaceId = workspaceId;
        this.title = title;
        this.background = background;
        this.image = image;
    }
}
