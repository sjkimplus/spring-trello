package com.sparta.springtrello.domain.kanban.dto.response;

import lombok.Getter;

@Getter
public class KanbanSimpleResponseDto {
    private String title;

    public KanbanSimpleResponseDto(String title) {
        this.title = title;
    }
}
