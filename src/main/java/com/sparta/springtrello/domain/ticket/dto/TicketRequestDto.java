package com.sparta.springtrello.domain.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor //테스트용
public class TicketRequestDto {

    private String title;
    private String contents;
    private String deadline;
    private Long kanbanId;
}
