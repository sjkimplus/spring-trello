package com.sparta.springtrello.domain.ticket.dto;

import lombok.Getter;

@Getter
public class TicketResponseDto {

    private String title;
    private String contents;
    private String deadline;
    private Long kanbanId;

    public TicketResponseDto (String title, String contents, String deadline, Long kanbanId, Long userId){
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
        this.kanbanId = kanbanId;
    }
}
