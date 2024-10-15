package com.sparta.springtrello.domain.kanban.entity;

import com.sparta.springtrello.common.Status;
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
    private Status status;

    public Kanban(Integer kanbanOrder,String title, Board board){
        this.kanbanOrder = kanbanOrder;
        this.title = title;
        this.board = board;
        this.status = Status.ACTIVATED;
    }

    public void updateKanban(String title, Board board){
        this.title = title;
        this.board = board;
        this.status = Status.ACTIVATED;
    }

    public void updateOrder(Integer kanbanOrder){
        this.kanbanOrder = kanbanOrder;
    }

    public void deleteKanban(){
        this.status = Status.DELETED;
    }

}
