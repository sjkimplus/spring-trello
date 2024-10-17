package com.sparta.springtrello.domain.ticket.entity;

import com.sparta.springtrello.common.Status;
import com.sparta.springtrello.common.Timestamped;
import com.sparta.springtrello.domain.kanban.entity.Kanban;
import com.sparta.springtrello.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(
        name = "ticket",
        indexes = {
                @Index(name = "idx_ticket_workspace_id", columnList = "member_id"),
                @Index(name = "idx_ticket_title", columnList = "title"),  // 제목에 대한 인덱스
                @Index(name = "idx_ticket_contents", columnList = "contents"),  // 내용에 대한 인덱스
                @Index(name = "idx_ticket_deadline", columnList = "deadline"),
                @Index(name = "idx_kanban_board_id", columnList = "kanban_id")
        }
)
public class Ticket extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @Version
    private int version;
     */

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String deadline;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_id")
    private Kanban kanban;

    public Ticket(String title, String contents, String deadline, Member member, Kanban kanban) {
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
        this.member = member;
        this.kanban = kanban;
        this.status = Status.ACTIVATED;
    }

    public void update(String title, String contents, String deadline, Kanban kanban) {
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
        this.kanban = kanban;
    }

    public void delete() {
        this.status = Status.DELETED;
    }

}
