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

    private String title;

    private String contents;

    private String deadline;

    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lists_id")
    private Lists lists;
}
