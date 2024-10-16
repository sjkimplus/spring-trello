package com.sparta.springtrello.domain.kanban.dto.response;

import com.sparta.springtrello.domain.kanban.entity.Kanban;
import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class KanbanResponseDto {
    private Long id;
    private String title;
    private List<TicketResponseDto> ticketResponseDtos;

    public KanbanResponseDto(String title) {
        this.title = title;
    }

    public KanbanResponseDto(Kanban kanban, List<TicketResponseDto> ticketResponseDtos) {
        this.id = kanban.getId();
        this.title = kanban.getTitle();
        this.ticketResponseDtos = ticketResponseDtos;
    }
}