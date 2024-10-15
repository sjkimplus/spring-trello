package com.sparta.springtrello.domain.comment.service;


import com.sparta.springtrello.domain.comment.dto.request.CommentEditRequestDto;
import com.sparta.springtrello.domain.comment.dto.request.CommentSaveRequestDto;
import com.sparta.springtrello.domain.comment.dto.response.CommentEditResponseDto;
import com.sparta.springtrello.domain.comment.dto.response.CommentSaveResponseDto;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.entity.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import com.sparta.springtrello.domain.ticket.repository.TicketRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final UserService userService;
    private final TicketRepository ticketRepository;

    public CommentSaveResponseDto saveComment(AuthUser authUser, Long cardId, CommentSaveRequestDto commentSaveRequestDto) {

        Long userId = authUser.getId();
        userService.checkUser(userId);

        Ticket ticket = ticketRepository.findById(cardId).orElseThrow(() ->
                new IllegalArgumentException("card없음"));

        Member member = memberRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalArgumentException("멤버 등록안됨"));


        if (!member.getMemberRole().equals(MemberRole.CREATOR)) {
            throw new IllegalArgumentException("CREATOR만 수정, 삭제할 수 있음.");
        }

        Comment comment = commentRepository.save(new Comment(commentSaveRequestDto, ticket, member));

        return new CommentSaveResponseDto(comment);
    }

    public CommentEditResponseDto editComment(AuthUser authUser, Long id, Long cardId, CommentEditRequestDto commentEditRequestDto) {

        Long userId = authUser.getId();
        userService.checkUser(userId);

        Ticket ticket = ticketRepository.findById(cardId).orElseThrow(() ->
                new IllegalArgumentException("card없음"));

        Member member = memberRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalArgumentException("멤버 등록안됨"));

        if (!member.getMemberRole().equals(MemberRole.CREATOR)) {
            throw new IllegalArgumentException("CREATOR만 수정, 삭제할 수 있음.");
        }

        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 없음"));

        comment.update(commentEditRequestDto);
        commentRepository.save(comment);

        return new CommentEditResponseDto(comment);
    }

    public void deleteComment(Long id, Long cardId, AuthUser authUser) {

        Long userId = authUser.getId();
        userService.checkUser(userId);

        Ticket ticket = ticketRepository.findById(cardId).orElseThrow(() ->
                new IllegalArgumentException("card없음"));

        Member member = memberRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalArgumentException("멤버 등록안됨"));

        if (!member.getMemberRole().equals(MemberRole.CREATOR)) {
            throw new IllegalArgumentException("CREATOR만 수정, 삭제할 수 있음.");
        }

        commentRepository.deleteById(id);
    }
}
