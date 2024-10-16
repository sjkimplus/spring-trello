package com.sparta.springtrello.domain.ticket.service;

import com.sparta.springtrello.common.exception.ErrorCode;
import com.sparta.springtrello.common.exception.HotSixException;
import com.sparta.springtrello.domain.kanban.entity.Kanban;
import com.sparta.springtrello.domain.kanban.repository.KanbanRepository;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.entity.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.ticket.dto.TicketRequestDto;
import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import com.sparta.springtrello.domain.ticket.dto.TicketUpdateDto;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import com.sparta.springtrello.domain.ticket.repository.TicketQueryDslRepository;
import com.sparta.springtrello.domain.ticket.repository.TicketRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final TicketQueryDslRepository ticketQueryDslRepository;
    private final KanbanRepository kanbanRepository;

    @Transactional
    public TicketResponseDto createTicket(AuthUser authUser, TicketRequestDto requestDto) {

        //ticket entity에 등록될 kanban 찾기
        Kanban kanban = kanbanRepository.findById(requestDto.getKanbanId()).orElseThrow();

        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findByWorkspaceIdAndUserId(kanban.getBoard().getWorkspace().getId(),authUser.getId())
                .orElseThrow(()-> new HotSixException(ErrorCode.USER_NOT_FOUND));

        //ticket을 등록하려는 유저의 role이 reader라면 금지
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)){
            throw new RuntimeException();
        }



        Ticket ticket = new Ticket(
                requestDto.getTitle(),
                requestDto.getContents(),
                requestDto.getDeadline(),
                member,
                kanban
        );
        ticketRepository.save(ticket);

        return new TicketResponseDto(
                ticket.getTitle(),
                ticket.getContents(),
                ticket.getDeadline(),
                kanban.getId()
        );

    }

    public TicketResponseDto getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        return new TicketResponseDto(
                ticket.getTitle(),
                ticket.getContents(),
                ticket.getDeadline(),
                ticket.getKanban().getId()
        );

    }

    @Transactional
    public TicketResponseDto updateTicket(AuthUser authUser, Long id, TicketUpdateDto requestDto) {
        //ticket entity에 등록될 kanban 찾기
        Kanban kanban = kanbanRepository.findById(requestDto.getKanbanId()).orElseThrow();

        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findByWorkspaceIdAndUserId(kanban.getBoard().getWorkspace().getId(),authUser.getId())
                .orElseThrow(()-> new HotSixException(ErrorCode.USER_NOT_FOUND));

        //ticket을 등록하려는 유저의 role이 reader인지 확인
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)){
            throw new RuntimeException();
        }

        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        ticket.update(
                requestDto.getTitle(),
                requestDto.getContents(),
                requestDto.getDeadline(),
                kanban
        );

        return new TicketResponseDto(
                ticket.getTitle(),
                ticket.getContents(),
                ticket.getDeadline(),
                ticket.getKanban().getId()
        );
    }


    public void deleteTicket(AuthUser authUser, Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findByWorkspaceIdAndUserId(ticket.getKanban().getBoard().getWorkspace().getId(),authUser.getId())
                .orElseThrow(()-> new HotSixException(ErrorCode.USER_NOT_FOUND));

        //ticket을 등록하려는 유저의 role이 reader인지 확인
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)) throw new RuntimeException();

        ticket.delete();
    }

    public Page<TicketResponseDto> searchTickets(int page, int size, long workspaceId, String ticketKeyword, String managerName, String deadline, String boardId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ticketQueryDslRepository.searchTickets(workspaceId, ticketKeyword, managerName, deadline, boardId, pageable);
    }
}
