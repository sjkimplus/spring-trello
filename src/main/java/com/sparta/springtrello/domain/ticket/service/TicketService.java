package com.sparta.springtrello.domain.ticket.service;

import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.ticket.dto.TicketRequestDto;
import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import com.sparta.springtrello.domain.ticket.dto.TicketUpdateDto;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import com.sparta.springtrello.domain.ticket.repository.TicketRepository;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public Object createTicket(SignUser signUser, TicketRequestDto requestDto) {

        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findById(signUser.getId());

        //ticket을 등록하려는 유저의 role이 createor인지 확인
        if (!member.getRole().equals("CREATOR")) throw new RuntimeException();

        //ticket entity에 등록될 kanban 찾기
        Kanban kanban = kanbanRepository.findById(requestDto.getKanbanId());

        //ticket entity에 등록될 User
        User user = userRepository.findById(signUser.getId());

        Ticket ticket = new Ticket(
                requestDto.getTitle(),
                requestDto.getContents(),
                requestDto.getDeadline(),
                user,
                kanban
        );

        return new TicketResponseDto(
                ticket.getTitle(),
                ticket.getContents(),
                ticket.getDeadline(),
                kanban.getId(),
                user.getId()
        );

    }

    public Object getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        return new TicketResponseDto(
                ticket.getTitle(),
                ticket.getContents(),
                ticket.getDeadline(),
                ticket.getKanban().getId(),
                ticket.getUser().getId()
        );

    }

    @Transactional
    public Object updateTicket(SignUser signUser, Long id, TicketUpdateDto requestDto) {
        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findById(signUser.getId());

        //ticket을 등록하려는 유저의 role이 createor인지 확인
        if (!member.getRole().equals("CREATOR")) throw new RuntimeException();

        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        //ticket entity에 등록될 kanban 찾기
        Kanban kanban = kanbanRepository.findById(requestDto.getKanbanId());

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
                ticket.getKanban().getId(),
                ticket.getUser().getId()
        );
    }


    public void deleteTicket(SignUser signUser, Long id) {
        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findById(signUser.getId());

        //ticket을 등록하려는 유저의 role이 createor인지 확인
        if (!member.getRole().equals("CREATOR")) throw new RuntimeException();

        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        ticket.delete();
    }
}
