package com.sparta.springtrello.domain.card.entity;

import com.sparta.springtrello.common.entity.Timestamped;
import com.sparta.springtrello.domain.kanban.entity.kanban;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Card extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    private String deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lists_id")
    private kanban kanban;
}
