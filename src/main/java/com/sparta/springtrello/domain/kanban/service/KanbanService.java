package com.sparta.springtrello.domain.kanban.service;

import com.sparta.springtrello.domain.kanban.repository.KanbanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KanbanService {

    private final KanbanRepository kanbanRepository;
}
