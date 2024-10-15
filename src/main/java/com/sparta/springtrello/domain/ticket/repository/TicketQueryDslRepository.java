package com.sparta.springtrello.domain.ticket.repository;

import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TicketQueryDslRepository {
    Page<TicketResponseDto> searchTickets(
            long workspaceId,
            String ticketKeyword,
            String managerName,
            String deadline,
            long boardId,
            Pageable pageable
    );
}
