package com.sparta.springtrello.domain.kanban.dto.response;

import lombok.Getter;

@Getter
public class KanbanResponseDto {
    private String title;

    public KanbanResponseDto(String title) {
        this.title = title;
    }
}
