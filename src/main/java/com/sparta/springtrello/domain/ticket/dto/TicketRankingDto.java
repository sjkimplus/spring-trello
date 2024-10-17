package com.sparta.springtrello.domain.ticket.dto;

import lombok.Getter;

@Getter
public class TicketRankingDto {
    private Long ticketId;
    private int viewCount;

    public TicketRankingDto(Long ticketId, int viewCount) {
        this.ticketId = ticketId;
        this.viewCount = viewCount;
    }
}
