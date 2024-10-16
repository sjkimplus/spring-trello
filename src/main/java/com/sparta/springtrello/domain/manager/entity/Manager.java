package com.sparta.springtrello.domain.manager.entity;

import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Manager(Ticket ticket, Member member) {
        this.ticket = ticket;
        this.member = member;
    }
}
