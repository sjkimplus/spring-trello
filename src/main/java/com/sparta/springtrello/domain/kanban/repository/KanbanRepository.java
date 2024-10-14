package com.sparta.springtrello.domain.kanban.repository;

import com.sparta.springtrello.domain.kanban.entity.kanban;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanRepository extends JpaRepository<kanban, Long> {

}
