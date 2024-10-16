package com.sparta.springtrello.domain.ticketfile.entity;

import com.sparta.springtrello.domain.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class TicketFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(nullable = false)
    private String fileUrl;

    public TicketFile(Ticket ticket, String fileUrl) {
        this.ticket = ticket;
        this.fileUrl = fileUrl;
    }

    // 파일업로드
    public void fileUpload(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    // 파일 삭제
    public void deleteFile() {
        this.fileUrl = null;
    }
}
