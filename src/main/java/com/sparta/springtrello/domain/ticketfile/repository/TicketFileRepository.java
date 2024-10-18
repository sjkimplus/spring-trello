package com.sparta.springtrello.domain.ticketfile.repository;

import com.sparta.springtrello.domain.ticketfile.entity.TicketFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketFileRepository extends JpaRepository<TicketFile, Long> {
}
