package com.sparta.springtrello.domain.ticketviewhistory.repository;

import com.sparta.springtrello.domain.ticketviewhistory.entity.TicketViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TicketViewHistoryRepository extends JpaRepository<TicketViewHistory,Long> {
    boolean existsByUserIdAndTicketIdAndViewDate(Long userId, Long ticketId, LocalDate viewDate);
}
