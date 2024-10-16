package com.sparta.springtrello.domain.ticketviewhistory.entity;

import com.sparta.springtrello.domain.ticket.entity.Ticket;
import com.sparta.springtrello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class TicketViewHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ticket_id",nullable = false)
    private Ticket ticket;

    private LocalDate viewDate;

    public TicketViewHistory(User user,Ticket ticket,LocalDate viewDate){
        this.user = user;
        this.ticket = ticket;
        this.viewDate = viewDate;
    }
}
