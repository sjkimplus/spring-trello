package com.sparta.springtrello.domain.comment.entity;

import com.sparta.springtrello.common.Timestamped;
import com.sparta.springtrello.domain.comment.dto.request.CommentEditRequestDto;
import com.sparta.springtrello.domain.comment.dto.request.CommentSaveRequestDto;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Comment(CommentSaveRequestDto commentSaveRequestDto) {
        this.content = commentSaveRequestDto.getContent();
    }

    public void update(CommentEditRequestDto commentEditRequestDto) {
        this.content = commentEditRequestDto.getContent();
    }
}
