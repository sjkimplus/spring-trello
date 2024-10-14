package com.sparta.springtrello.domain.kanban.repository;

import com.sparta.springtrello.domain.kanban.entity.Kanban;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanRepository extends JpaRepository<Kanban, Long> {

}
