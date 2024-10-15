package com.sparta.springtrello.domain.kanban.entity;

import com.sparta.springtrello.common.Timestamped;
import com.sparta.springtrello.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Getter
@NoArgsConstructor
@Table(name = "kanbans")
public class Kanban extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer kanbanOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Enumerated(EnumType.STRING)
    private KanbanStatus kanbanStatus;

    public Kanban(Integer kanbanOrder,String title, Board board, KanbanStatus kanbanStatus){
        this.kanbanOrder = kanbanOrder;
        this.title = title;
        this.board = board;
        this.kanbanStatus = kanbanStatus;
    }

    public void updateKanban(String title, Board board,KanbanStatus kanbanStatus){
        this.title = title;
        this.board = board;
        this.kanbanStatus = kanbanStatus;
    }

    public void updateOrder(Integer kanbanOrder){
        this.kanbanOrder = kanbanOrder;
    }

    public void deleteKanban(KanbanStatus kanbanStatus){
        this.kanbanStatus = kanbanStatus;
    }

}
