package com.sparta.springtrello.domain.ticket.dto;

import lombok.Getter;

@Getter
public class TicketSearchResponseDto {

    private String title;
    private String contents;
    private String deadline;
    private Long kanbanId;

    public TicketSearchResponseDto (String title, String contents, String deadline, Long kanbanId){
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
        this.kanbanId = kanbanId;
    }
}