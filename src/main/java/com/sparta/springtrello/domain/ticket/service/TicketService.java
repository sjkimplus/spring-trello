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
import com.sparta.springtrello.domain.member.entity.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.ticket.dto.TicketDetailResponseDto;
import com.sparta.springtrello.domain.ticket.dto.TicketRequestDto;
import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import com.sparta.springtrello.domain.ticket.repository.TicketQueryDslRepository;
import com.sparta.springtrello.domain.ticket.repository.TicketRepository;
import com.sparta.springtrello.domain.ticketviewhistory.entity.TicketViewHistory;
import com.sparta.springtrello.domain.ticketviewhistory.repository.TicketViewHistoryRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final TicketQueryDslRepository ticketQueryDslRepository;
    private final KanbanRepository kanbanRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ManagerRepository managerRepository;
    private final TicketViewHistoryRepository ticketViewHistoryRepository;


    @Transactional
    public TicketResponseDto createTicket(AuthUser authUser, TicketRequestDto requestDto) {

        //ticket entity에 등록될 kanban 찾기
        Kanban kanban = kanbanRepository.findById(requestDto.getKanbanId()).orElseThrow(() ->
                new HotSixException(ErrorCode.KANBAN_NOT_FOUND));

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

        return new TicketResponseDto(ticket);

    }

    @Transactional
    public TicketDetailResponseDto getTicket(AuthUser authUser,Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.TICKET_NOT_FOUND));
        //조회수 중복 증가 방지 위한 시간
        LocalDate today = LocalDate.now();
        //조회수 중복 방지를 위한 user생성
        User user = User.fromAuthUser(authUser);
        //이미 본 user인지 판별
        boolean alreadyViewed = ticketViewHistoryRepository.existsByUserIdAndTicketIdAndViewDate(user.getId(),id,today);
        //본유저가 아니라면 해당 ticket의 조회수 증가
        if(!alreadyViewed){
            ticket.increaseViewCount();
            ticketRepository.save(ticket);

            TicketViewHistory history = new TicketViewHistory(user,ticket,today);
            ticketViewHistoryRepository.save(history);
        }

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

        Ticket ticket = ticketRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.TICKET_NOT_FOUND));

        //ticket entity에 등록될 kanban 찾기
        Kanban kanban = kanbanRepository.findById(requestDto.getKanbanId()).orElseThrow(() ->
                new HotSixException(ErrorCode.KANBAN_NOT_FOUND));

        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findByWorkspaceIdAndUserId(kanban.getBoard().getWorkspace().getId(),authUser.getId())
                .orElseThrow(()-> new HotSixException(ErrorCode.USER_NOT_FOUND));

        //ticket을 등록하려는 유저의 role이 reader인지 확인
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)){
            throw new RuntimeException();
        }


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

        Ticket ticket = ticketRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.TICKET_NOT_FOUND));

        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findByWorkspaceIdAndUserId(ticket.getKanban().getBoard().getWorkspace().getId(),authUser.getId())
                .orElseThrow(()-> new HotSixException(ErrorCode.USER_NOT_FOUND));

        //ticket을 등록하려는 유저의 role이 reader인지 확인
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)) throw new RuntimeException();

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

        return getTicket(authUser,id);

    }

    public Page<TicketResponseDto> searchTickets(int page, int size, long workspaceId, String ticketKeyword, String managerName, String deadline, String boardId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ticketQueryDslRepository.searchTickets(workspaceId, ticketKeyword, managerName, deadline, boardId, pageable);
    }

    @Scheduled(cron = "0 35 04 * * ?")
    @Transactional
    public void resetDailyViewCount(){
        ticketRepository.resetDailyViewCount();
    }

}
