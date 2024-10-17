package com.sparta.springtrello.domain.ticket.repository;

import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TicketQueryDslRepository {
    Page<TicketResponseDto> searchTickets(
            long workspaceId,
            String ticketTitle,
            String ticketContents,
            String managerName,
            String deadline,
            String boardId,
            Pageable pageable
    );
}
