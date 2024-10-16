package com.sparta.springtrello.domain.ticketfile.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TicketFileRequestDto {
    private Long ticketId;
    private String fileUrl;
}