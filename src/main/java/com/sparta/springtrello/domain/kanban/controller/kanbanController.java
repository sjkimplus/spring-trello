package com.sparta.springtrello.domain.kanban.controller;

import com.sparta.springtrello.domain.kanban.service.KanbanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class kanbanController {

    private final KanbanService kanbanService;
}
