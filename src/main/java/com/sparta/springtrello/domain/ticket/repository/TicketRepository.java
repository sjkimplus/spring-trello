package com.sparta.springtrello.domain.ticket.repository;

import com.sparta.springtrello.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByKanban_id(Long id);

//    @Modifying
//    @Query("UPDATE Ticket t SET t.dailyViewCount = 0")
//    void resetDailyViewCount();
}
