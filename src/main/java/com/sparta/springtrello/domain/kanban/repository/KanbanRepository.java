package com.sparta.springtrello.domain.kanban.repository;

import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.kanban.entity.Kanban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KanbanRepository extends JpaRepository<Kanban, Long> {

    @Query("SELECT MAX(k.kanbanOrder) FROM Kanban k WHERE k.board = :board")
    Integer findMaxOrderByBoard(@Param("board") Board board);

    @Modifying
    @Query("UPDATE Kanban k SET k.kanbanOrder = k.kanbanOrder + 1 WHERE k.board = :board AND k.kanbanOrder BETWEEN :startOrder AND :endOrder")
    void increaseOrderBetween(@Param("board") Board board, @Param("startOrder") Integer startOrder, @Param("endOrder") Integer endOrder);

    @Modifying
    @Query("UPDATE Kanban k SET k.kanbanOrder = k.kanbanOrder - 1 WHERE k.board = :board AND k.kanbanOrder BETWEEN :startOrder AND :endOrder")
    void decreaseOrderBetween(@Param("board") Board board, @Param("startOrder") Integer startOrder, @Param("endOrder") Integer endOrder);

}
