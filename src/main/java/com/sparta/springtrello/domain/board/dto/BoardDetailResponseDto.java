package com.sparta.springtrello.domain.board.dto;

import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.kanban.dto.response.KanbanResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardDetailResponseDto {

    private Long memberId;
    private Long workspaceId;
    private String title;
    private String background;
    private String image;
    private List<KanbanResponseDto> kanbanResponseDtos;

    public BoardDetailResponseDto(Board board, List<KanbanResponseDto> kanbanResponseDtos) {
        this.memberId = board.getMember().getId();
        this.workspaceId = board.getWorkspace().getId();
        this.title = board.getTitle();
        this.background = board.getBackground();
        this.image = board.getImage();
        this.kanbanResponseDtos = kanbanResponseDtos;
    }
}
