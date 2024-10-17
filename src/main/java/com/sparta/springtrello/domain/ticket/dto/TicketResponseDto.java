package com.sparta.springtrello.domain.ticket.dto;

import com.sparta.springtrello.domain.ticket.entity.Ticket;
import lombok.Getter;

@Getter
public class TicketResponseDto {

    private String title;
    private String contents;
    private String deadline;
    private Long kanbanId;
    private Long memberId;

    public TicketResponseDto (Ticket ticket){
        this.title = ticket.getTitle();
        this.contents = ticket.getContents();
        this.deadline = ticket.getDeadline();
        this.kanbanId = ticket.getKanban().getId();
        this.memberId = ticket.getMember().getId();
    }
}
