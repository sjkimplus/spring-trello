package com.sparta.springtrello.domain.manager.repository;

import com.sparta.springtrello.domain.manager.entity.Manager;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    List<Manager> findAllByTicket(Ticket ticket);
}
