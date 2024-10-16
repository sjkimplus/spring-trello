package com.sparta.springtrello.domain.board.dto;

import com.sparta.springtrello.domain.board.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {

    private Long memberId;
    private Long workspaceId;
    private String title;
    private String background;
    private String image;

    public BoardResponseDto(Board board) {
        this.memberId = board.getMember().getId();
        this.workspaceId = board.getWorkspace().getId();
        this.title = board.getTitle();
        this.background = board.getBackground();
        this.image = board.getImage();
    }
}
