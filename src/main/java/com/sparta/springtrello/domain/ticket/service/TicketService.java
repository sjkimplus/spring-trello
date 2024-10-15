package com.sparta.springtrello.domain.ticket.service;

import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.ticket.dto.TicketRequestDto;
import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

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
}
