package com.sparta.springtrello.domain.comment.service;


import com.sparta.springtrello.common.exception.ErrorCode;
import com.sparta.springtrello.common.exception.HotSixException;
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
                new HotSixException(ErrorCode.TICKET_NOT_FOUND));

        Member member = memberRepository.findByUserId(userId).orElseThrow(() ->
                new HotSixException(ErrorCode.USER_NO_AUTHORITY));


        if (member.getMemberRole().equals(MemberRole.ROLE_READER)) {
            throw new HotSixException(ErrorCode.USER_NO_AUTHORITY);
        }

        Comment comment = commentRepository.save(new Comment(commentSaveRequestDto, ticket, member));

        return new CommentSaveResponseDto(comment);
    }

    public CommentEditResponseDto editComment(AuthUser authUser, Long id, Long cardId, CommentEditRequestDto commentEditRequestDto) {

        Long userId = authUser.getId();
        userService.checkUser(userId);

        Ticket ticket = ticketRepository.findById(cardId).orElseThrow(() ->
                new HotSixException(ErrorCode.TICKET_NOT_FOUND));

        Member member = memberRepository.findByUserId(userId).orElseThrow(() ->
                new HotSixException(ErrorCode.USER_NO_AUTHORITY));

        if (member.getMemberRole().equals(MemberRole.ROLE_READER)) {
            throw new HotSixException(ErrorCode.USER_NO_AUTHORITY);
        }

        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.COMMENT_NOT_FOUND));

        comment.update(commentEditRequestDto);
        commentRepository.save(comment);

        return new CommentEditResponseDto(comment);
    }

    public void deleteComment(Long id, Long cardId, AuthUser authUser) {

        Long userId = authUser.getId();
        userService.checkUser(userId);

        Ticket ticket = ticketRepository.findById(cardId).orElseThrow(() ->
                new HotSixException(ErrorCode.TICKET_NOT_FOUND));

        Member member = memberRepository.findByUserId(userId).orElseThrow(() ->
                new HotSixException(ErrorCode.USER_NO_AUTHORITY));

        if (member.getMemberRole().equals(MemberRole.ROLE_READER)) {
            throw new HotSixException(ErrorCode.USER_NO_AUTHORITY);
        }

        commentRepository.deleteById(id);
    }
}
