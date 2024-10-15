package com.sparta.springtrello.domain.ticket.dto;

import lombok.Getter;

@Getter
public class TicketRequestDto {

    private String title;
    private String contents;
    private String deadline;
    private Long kanbanId;
}
