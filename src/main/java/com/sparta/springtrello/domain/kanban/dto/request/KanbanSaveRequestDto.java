package com.sparta.springtrello.domain.kanban.dto.request;

import com.sparta.springtrello.domain.kanban.entity.KanbanStatus;
import lombok.Getter;

@Getter
public class KanbanSaveRequestDto {
    private String title;
    private KanbanStatus kanbanStatus;
}
