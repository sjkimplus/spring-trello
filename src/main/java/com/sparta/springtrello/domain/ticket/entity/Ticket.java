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

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_id")
    private Kanban kanban;

    @Column(name = "views")
    private Integer dailyViewCount = 0;

    @Column(name = "totalviews")
    private Integer totalViewCount = 0;

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

    public void increaseViewCount(){
        this.dailyViewCount ++;
        this.totalViewCount ++;//조회수 증가 메서드
    }
}
