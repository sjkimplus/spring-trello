package com.sparta.springtrello.domain.ticket.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.sparta.springtrello.common.exception.ErrorCode;
import com.sparta.springtrello.common.exception.HotSixException;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
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
import com.sparta.springtrello.domain.ticket.dto.*;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import com.sparta.springtrello.domain.ticket.repository.TicketQueryDslRepository;
import com.sparta.springtrello.domain.ticket.repository.TicketRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.user.service.UserService;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    private final RedisTemplate<String, Object> viewCountRepository;
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;


    @Transactional
    public TicketResponseDto createTicket(AuthUser authUser, TicketRequestDto requestDto) {

        //ticket entity에 등록될 kanban 찾기
        Kanban kanban = kanbanRepository.findById(requestDto.getKanbanId()).orElseThrow(() ->
                new HotSixException(ErrorCode.KANBAN_NOT_FOUND));

        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findByWorkspaceIdAndUserId(kanban.getBoard().getWorkspace().getId(), authUser.getId())
                .orElseThrow(() -> new HotSixException(ErrorCode.USER_NOT_FOUND));

        //ticket을 등록하려는 유저의 role이 reader라면 금지
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)) {
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
    public TicketDetailResponseDto getTicket(AuthUser authUser, Long id) {
        String cacheKey = "ticket:detail:" + id;

        // Redis에서 캐시 조회
        TicketDetailResponseDto cachedResponse = (TicketDetailResponseDto) viewCountRepository.opsForValue().get(cacheKey);

        if (cachedResponse != null) {
            return cachedResponse;
        }
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() ->
                new HotSixException(ErrorCode.TICKET_NOT_FOUND));

        //조회수 중복 방지를 위한 user생성
        User user = User.fromAuthUser(authUser);
        //이미 본 user인지 판별
        String ticketViews = "ticket:user:" + id + ":" + user.getId();
        Object value = viewCountRepository.opsForValue().get(ticketViews);
        boolean alreadyViewed = value != null && Boolean.parseBoolean(value.toString());
        //본유저가 아니라면 해당 ticket의 조회수 증가
        if (!alreadyViewed) {
            //redis에서 조회수 가져오기
            String viewCount = "ticket:view:" + id;
            String currentViewCount = (String) viewCountRepository.opsForValue().get(viewCount);
            int newViewCount = (currentViewCount != null ? Integer.parseInt(currentViewCount) : 0) + 1;

            // 사용자 조회 기록 저장
            viewCountRepository.opsForValue().set(ticketViews, "true");  // Redis에 "true"로 저장
            // Redis에 조회수 업데이트
            viewCountRepository.opsForValue().set("ticket:view:" + id, String.valueOf(newViewCount));
        }

        List<Comment> commentList = commentRepository.findAllByTicket(ticket);
        List<CommentSaveResponseDto> commentSaveResponseDtoList = commentList.stream()
                .map(CommentSaveResponseDto::new).toList();

        List<Manager> managerList = managerRepository.findAllByTicket(ticket);
        List<Long> memberList = new ArrayList<>();
        for (Manager manager : managerList) {
            memberList.add(manager.getMember().getUser().getId());
        }

        // DB에서 조회한 정보를 DTO로 변환
        TicketDetailResponseDto responseDto = new TicketDetailResponseDto(
                ticket.getTitle(),
                ticket.getContents(),
                ticket.getDeadline(),
                ticket.getKanban().getId(),
                commentSaveResponseDtoList,
                memberList
        );
        // Redis에 캐시 저장
        viewCountRepository.opsForValue().set(cacheKey, responseDto);

        return responseDto;
    }

    @Transactional
    public TicketResponseDto updateTicket(AuthUser authUser, Long id, TicketRequestDto requestDto) {

        Ticket ticket = ticketRepository.findByIdWithPessimisticLock(id).orElseThrow(() ->
                new HotSixException(ErrorCode.TICKET_NOT_FOUND));

        //ticket entity에 등록될 kanban 찾기
        Kanban kanban = kanbanRepository.findById(requestDto.getKanbanId()).orElseThrow(() ->
                new HotSixException(ErrorCode.KANBAN_NOT_FOUND));

        //ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findByWorkspaceIdAndUserId(kanban.getBoard().getWorkspace().getId(), authUser.getId())
                .orElseThrow(() -> new HotSixException(ErrorCode.USER_NOT_FOUND));

        //ticket을 등록하려는 유저의 role이 reader인지 확인
        if (member.getMemberRole().equals(MemberRole.ROLE_READER)) {
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
        Member member = memberRepository.findByWorkspaceIdAndUserId(ticket.getKanban().getBoard().getWorkspace().getId(), authUser.getId())
                .orElseThrow(() -> new HotSixException(ErrorCode.USER_NOT_FOUND));

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
        for (Long memberId : memberList) {
            Member member = memberRepository.findById(memberId).orElseThrow();
            Manager manager = new Manager(ticket, member);
            managerRepository.save(manager);
        }

        return getTicket(authUser, id);

    }

    public Page<TicketSearchResponseDto> searchTickets(int page, int size, long workspaceId, String ticketTitle, String ticketContents, String managerName, String deadline, String boardId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ticketQueryDslRepository.searchTickets(workspaceId, ticketTitle, ticketContents, managerName, deadline, boardId, pageable);
    }


    @Transactional
    public String pushTickets() {
        int batchSize = 100;
        List<Ticket> tickets = new ArrayList<>(batchSize);
        Set<String> existingTitles = new HashSet<>();

        // 예시로 생성된 Workspace와 User를 사용합니다. 실제로는 데이터베이스에서 조회해야 할 수 있습니다.
        Workspace workspace = workspaceRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("워크스페이스를 찾을 수 없습니다."));
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("워크스페이스를 찾을 수 없습니다."));

        Board board = new Board(member, workspace, "Sample Board", "blue", null);
        boardRepository.save(board);

        Kanban kanban = new Kanban(1, "Sample Kanban", board);
        kanbanRepository.save(kanban);

        for (int i = 1; i <= 1000000; i++) {
            String randomTitle;

            do {
                randomTitle = generateRandomTitle();
            } while (existingTitles.contains(randomTitle));
            existingTitles.add(randomTitle);


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

    public List<TicketRankingDto> getDailyViewRanking() {
        Set<String> keys = viewCountRepository.keys("ticket:view:*");

        Map<Long,Integer> viewCounts = new HashMap<>();

        for (String key : keys) {
            String[] parts = key.split(":");
            Long ticketId = Long.parseLong(parts[2]); // `ticket:view:티켓ID`
            String countStr = (String) viewCountRepository.opsForValue().get(key);

            if (countStr != null) {
                int count = Integer.parseInt(countStr);
                viewCounts.put(ticketId, viewCounts.getOrDefault(ticketId, 0) + count);
            }
        }

        // 랭킹 정렬
       return viewCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // 높은 조회수 우선
                .map(entry -> new TicketRankingDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 0 * * ?") // 자정마다 실행
    @Transactional
    public void saveViewCountsToDB() {
        Set<String> keys = viewCountRepository.keys("ticket:*");
        // Redis 캐시 초기화
        viewCountRepository.delete(keys);
    }

}