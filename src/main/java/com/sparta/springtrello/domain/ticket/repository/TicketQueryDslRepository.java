package com.sparta.springtrello.domain.ticket.repository;

import com.sparta.springtrello.domain.ticket.dto.TicketSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketQueryDslRepository {
    Page<TicketSearchResponseDto> searchTickets(
            long workspaceId,
            String ticketTitle,
            String ticketContents,
            String managerName,
            String deadline,
            String boardId,
            Pageable pageable
    );
}
