package com.sparta.springtrello.domain.ticket.service;

import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.kanban.entity.Kanban;
import com.sparta.springtrello.domain.kanban.repository.KanbanRepository;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.entity.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.ticket.dto.TicketRequestDto;
import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import com.sparta.springtrello.domain.ticket.dto.TicketUpdateDto;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import com.sparta.springtrello.domain.ticket.repository.TicketRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final KanbanRepository kanbanRepository;
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    @Transactional
    public TicketResponseDto createTicket(AuthUser authUser, TicketRequestDto requestDto) {

        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findById(authUser.getId()).orElseThrow();

        //ticket을 등록하려는 유저의 role이 reader라면 금지
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)){
            throw new RuntimeException();
        }

        //ticket entity에 등록될 kanban 찾기
        Kanban kanban = kanbanRepository.findById(requestDto.getKanbanId()).orElseThrow();

        Ticket ticket = new Ticket(
                requestDto.getTitle(),
                requestDto.getContents(),
                requestDto.getDeadline(),
                member,
                kanban
        );

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
        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findById(authUser.getId()).orElseThrow();

        //ticket을 등록하려는 유저의 role이 reader인지 확인
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)){
            throw new RuntimeException();
        }

        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        //ticket entity에 등록될 kanban 찾기
        Kanban kanban = kanbanRepository.findById(requestDto.getKanbanId()).orElseThrow();

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
        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findById(authUser.getId()).orElseThrow();

        //ticket을 등록하려는 유저의 role이 reader인지 확인
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)) throw new RuntimeException();

        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        ticket.delete();
    }

    @Transactional
    public String pushTickets() {
        int batchSize = 100;
        List<Ticket> tickets = new ArrayList<>(batchSize);
        Set<String> existingTitles = new HashSet<>();

        // 예시로 생성된 Workspace와 User를 사용합니다. 실제로는 데이터베이스에서 조회해야 할 수 있습니다.
        Workspace workspace = workspaceRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("워크스페이스를 찾을 수 없습니다."));
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Board board = new Board(user, workspace, "Sample Board", "blue", null);
        boardRepository.save(board);

        Kanban kanban = new Kanban(1, "Sample Kanban", board);
        kanbanRepository.save(kanban);

        for (int i = 1; i <= 1000000; i++) {
            String randomTitle;

            do {
                randomTitle = generateRandomTitle();
            } while (existingTitles.contains(randomTitle));
            existingTitles.add(randomTitle);

            Member member = new Member(user, workspace, MemberRole.ROLE_BOARD);
            memberRepository.save(member);

            Ticket ticket = new Ticket(randomTitle, "contents", "deadline", member, kanban);
            tickets.add(ticket);

            if (i % batchSize == 0) {
                ticketRepository.saveAll(tickets);
                tickets.clear();
            }
        }

        if (!tickets.isEmpty()) {
            ticketRepository.saveAll(tickets);
        }

        return "Successfully saved 1 million random tickets.";
    }

    public String generateRandomTitle() {
        return UUID.randomUUID().toString().substring(0, 10);
    }
}