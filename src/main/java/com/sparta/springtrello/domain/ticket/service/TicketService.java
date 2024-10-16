package com.sparta.springtrello.domain.ticket.service;

import com.sparta.springtrello.common.exception.ErrorCode;
import com.sparta.springtrello.common.exception.HotSixException;
import com.sparta.springtrello.domain.comment.dto.response.CommentSaveResponseDto;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.kanban.entity.Kanban;
import com.sparta.springtrello.domain.kanban.repository.KanbanRepository;
import com.sparta.springtrello.domain.manager.dto.ManagerRequestDto;
import com.sparta.springtrello.domain.manager.entity.Manager;
import com.sparta.springtrello.domain.manager.repository.ManagerRepository;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.ticket.dto.TicketDetailResponseDto;
import com.sparta.springtrello.domain.ticket.dto.TicketRequestDto;
import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import com.sparta.springtrello.domain.ticket.repository.TicketRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final KanbanRepository kanbanRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ManagerRepository managerRepository;


    @Transactional
    public TicketResponseDto createTicket(AuthUser authUser, TicketRequestDto requestDto) {

        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findById(authUser.getId()).orElseThrow(() ->
                new HotSixException(ErrorCode.MEMBER_NOT_FOUND));

        //ticket을 등록하려는 유저의 role이 createor인지 확인
        if (!member.getMemberRole().toString().equals("CREATOR")) throw new HotSixException(ErrorCode.USER_NO_AUTHORITY);

        //ticket entity에 등록될 kanban 찾기
        Kanban kanban = kanbanRepository.findById(requestDto.getKanbanId()).orElseThrow(() ->
                new HotSixException(ErrorCode.KANBAN_NOT_FOUND));

        Ticket ticket = new Ticket(
                requestDto.getTitle(),
                requestDto.getContents(),
                requestDto.getDeadline(),
                member,
                kanban
        );

        ticketRepository.save(ticket);

        return new TicketResponseDto(ticket);

    }

    public TicketDetailResponseDto getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.TICKET_NOT_FOUND));

        List<Comment> commentList = commentRepository.findAllByTicket(ticket);
        List<CommentSaveResponseDto> commentSaveResponseDtoList = commentList.stream()
                .map(CommentSaveResponseDto::new).toList();

        List<Manager> managerList = managerRepository.findAllByTicket(ticket);
        List<Long> memberList = new ArrayList<>();
        for(Manager manager : managerList) {
            memberList.add(manager.getMember().getUser().getId());
        }

        return new TicketDetailResponseDto(
                ticket.getTitle(),
                ticket.getContents(),
                ticket.getDeadline(),
                ticket.getKanban().getId(),
                commentSaveResponseDtoList,
                memberList
        );

    }

    @Transactional
    public TicketResponseDto updateTicket(AuthUser authUser, Long id, TicketRequestDto requestDto) {
        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findById(authUser.getId()).orElseThrow(() ->
                new HotSixException(ErrorCode.MEMBER_NOT_FOUND));

        //ticket을 등록하려는 유저의 role이 createor인지 확인
        if (!member.getMemberRole().toString().equals("CREATOR")) throw new HotSixException(ErrorCode.USER_NO_AUTHORITY);

        Ticket ticket = ticketRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.TICKET_NOT_FOUND));

        //ticket entity에 등록될 kanban 찾기
        Kanban kanban = kanbanRepository.findById(requestDto.getKanbanId()).orElseThrow(() ->
                new HotSixException(ErrorCode.KANBAN_NOT_FOUND));

        ticket.update(
                requestDto.getTitle(),
                requestDto.getContents(),
                requestDto.getDeadline(),
                kanban
        );

        return new TicketResponseDto(ticket);
    }

    @Transactional
    public void deleteTicket(AuthUser authUser, Long id) {
        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findById(authUser.getId()).orElseThrow(() ->
                new HotSixException(ErrorCode.MEMBER_NOT_FOUND));

        //ticket을 등록하려는 유저의 role이 creator인지 확인
        if (!member.getMemberRole().toString().equals("CREATOR")) throw new HotSixException(ErrorCode.USER_NO_AUTHORITY);

        Ticket ticket = ticketRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.TICKET_NOT_FOUND));

        ticket.delete();
    }

    @Transactional
    public TicketDetailResponseDto addManagerToTicket(AuthUser authUser, Long id, ManagerRequestDto requestDto) {
        userService.checkUser(authUser.getId());
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.TICKET_NOT_FOUND));

        List<Long> memberList = requestDto.getMemberList();
        for(Long memberId : memberList) {
            Member member = memberRepository.findById(memberId).orElseThrow();
            Manager manager = new Manager(ticket, member);
            managerRepository.save(manager);
        }

        return getTicket(id);

    }

}
