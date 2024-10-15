package com.sparta.springtrello.domain.ticket.entity;

import com.sparta.springtrello.common.Status;
import com.sparta.springtrello.common.Timestamped;
import com.sparta.springtrello.domain.list.entity.Lists;
import com.sparta.springtrello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Ticket extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String deadline;

    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_id")
    private Kanban kanban;

    public Ticket(String title, String contents, String deadline, User user, Kanban kanban) {
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
        this.user = user;
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
