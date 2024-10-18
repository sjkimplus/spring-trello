package com.sparta.springtrello.domain.ticketfile.dto;

import lombok.Getter;

@Getter
public class TicketFileResponseDto {
    private Long id;
    private Long ticketId;
    private String fileUrl;

    public TicketFileResponseDto(Long id, Long ticketId, String fileUrl) {
        this.id = id;
        this.ticketId = ticketId;
        this.fileUrl = fileUrl;
    }
}