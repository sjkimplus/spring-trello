package com.sparta.springtrello.domain.list.entity;

import com.sparta.springtrello.common.Timestamped;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Getter
@NoArgsConstructor
@Table(name = "Lists")
public class Lists extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id",nullable = true)
    private Ticket ticket;
}
