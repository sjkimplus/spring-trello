//package com.sparta.springtrello.domain.ticket.service;
//
//import com.sparta.springtrello.domain.board.entity.Board;
//import com.sparta.springtrello.domain.board.repository.BoardRepository;
//import com.sparta.springtrello.domain.kanban.entity.Kanban;
//import com.sparta.springtrello.domain.kanban.repository.KanbanRepository;
//import com.sparta.springtrello.domain.member.entity.Member;
//import com.sparta.springtrello.domain.member.entity.MemberRole;
//import com.sparta.springtrello.domain.member.repository.MemberRepository;
//import com.sparta.springtrello.domain.ticket.dto.TicketRequestDto;
//import com.sparta.springtrello.domain.ticket.entity.Ticket;
//import com.sparta.springtrello.domain.ticket.repository.TicketRepository;
//import com.sparta.springtrello.domain.user.dto.AuthUser;
//import com.sparta.springtrello.domain.user.entity.User;
//import com.sparta.springtrello.domain.user.enums.UserRole;
//import com.sparta.springtrello.domain.user.enums.UserStatus;
//import com.sparta.springtrello.domain.user.repository.UserRepository;
//import com.sparta.springtrello.domain.workspace.entity.Workspace;
//import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.UUID;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//public class TicketLockTest {
//
//    @Autowired
//    private TicketRepository ticketRepository;
//
//    @Autowired
//    private KanbanRepository kanbanRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private TicketService ticketService;
//    @Autowired
//    private WorkspaceRepository workspaceRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private BoardRepository boardRepository;
//
//    @Test
//    void 비관락() throws InterruptedException {
//
//        User user = new User(1L, "email@email.com", UserRole.ROLE_ADMIN);
//        ReflectionTestUtils.setField(user, "name", "User");
//        ReflectionTestUtils.setField(user, "password", "Password");
//        ReflectionTestUtils.setField(user, "status", UserStatus.ACTIVATED);
//        userRepository.save(user);
//
//        Workspace workspace = new Workspace(1L, "title", "content", null);
//        workspaceRepository.save(workspace);
//
//        Member member = new Member(user, workspace, MemberRole.ROLE_WORKSPACE);
//        ReflectionTestUtils.setField(member, "id", 1L);
//        memberRepository.save(member);
//
//        Board board = new Board(member, workspace, "boardtitle", "background", "image");
//        ReflectionTestUtils.setField(board, "id", 1L);
//        boardRepository.save(board);
//
//        Kanban kanban = new Kanban(1, "kanbantitle", board);
//        ReflectionTestUtils.setField(kanban, "id", 1L);
//        kanbanRepository.save(kanban);
//
//        Ticket ticket = new Ticket("tickettitle","ticketcontent","2000-00-00",member,kanban);
//        ReflectionTestUtils.setField(ticket, "id", 1L);
//        Ticket savedTicket = ticketRepository.save(ticket);
//
//        AuthUser authUser = new AuthUser(1L, "email@email.com", UserRole.ROLE_ADMIN);
//        Long id = savedTicket.getId();
//
//        //when
//        int testCount = 1000;
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        CountDownLatch latch = new CountDownLatch(testCount);
//
//        AtomicInteger successfulUpdates = new AtomicInteger(0);
//
//        long startTime = System.currentTimeMillis();
//
//        for (int i = 0; i < testCount; i++) {
//            executorService.submit(() -> {
//                try {
//                    TicketRequestDto requestDto = new TicketRequestDto("title" + UUID.randomUUID(),
//                            "UpContents","3000-00-00",1L);
//                    ticketService.updateTicket(authUser, id, requestDto);
//                    successfulUpdates.incrementAndGet();
//                } catch (Exception e) {
//                    System.out.println("비관적 락 충돌: " + e.getMessage());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await(); // 모든 스레드가 작업을 완료할 때까지 대기
//        executorService.shutdown();
//
//        long endTime = System.currentTimeMillis();
//        long durationInMillis = endTime - startTime;
//        double durationInSeconds = durationInMillis / 1000.0;
//
//        System.out.println("성공한 업데이트 수: " + successfulUpdates.get());
//        System.out.println("테스트 실행 시간: " + durationInSeconds + "초");
//
//        assertEquals(successfulUpdates.get(), 1000);
//
//    }
//
///*
//    @Test
//    void 낙관락() throws InterruptedException {
//
//        User user = new User(1L, "email@email.com", UserRole.ROLE_ADMIN);
//        ReflectionTestUtils.setField(user, "name", "User");
//        ReflectionTestUtils.setField(user, "password", "Password");
//        ReflectionTestUtils.setField(user, "status", UserStatus.ACTIVATED);
//        userRepository.save(user);
//
//        Workspace workspace = new Workspace(1L, "title", "content", null);
//        workspaceRepository.save(workspace);
//
//        Member member = new Member(user, workspace, MemberRole.ROLE_WORKSPACE);
//        ReflectionTestUtils.setField(member, "id", 1L);
//        memberRepository.save(member);
//
//        Board board = new Board(member, workspace, "boardtitle", "background", "image");
//        ReflectionTestUtils.setField(board, "id", 1L);
//        boardRepository.save(board);
//
//        Kanban kanban = new Kanban(1, "kanbantitle", board);
//        ReflectionTestUtils.setField(kanban, "id", 1L);
//        kanbanRepository.save(kanban);
//
//        Ticket ticket = new Ticket("tickettitle","ticketcontent","2000-00-00",member,kanban);
//        ReflectionTestUtils.setField(ticket, "id", 1L);
//        Ticket savedTicket = ticketRepository.save(ticket);
//
//        AuthUser authUser = new AuthUser(1L, "email@email.com", UserRole.ROLE_ADMIN);
//        Long id = savedTicket.getId();
//
//
//        //when
//        int testCount = 10000;
//        ExecutorService executorService = Executors.newFixedThreadPool(20);
//        CountDownLatch latch = new CountDownLatch(testCount);
//
//        // 성공 횟수 추적 변수
//        AtomicInteger successfulUpdates = new AtomicInteger(0);
//
//        // 예외 발생 횟수를 추적하기 위한 변수
//        AtomicInteger optimisticLockExceptionCount = new AtomicInteger(0);
//
//        long startTime = System.currentTimeMillis();
//
//        for (int i = 0; i < testCount; i++) {
//            executorService.submit(() -> {
//                try {
//                    TicketRequestDto requestDto = new TicketRequestDto("title" + UUID.randomUUID(),
//                            "UpContents","3000-00-00",1L);
//                    ticketService.updateTicket(authUser, id, requestDto);
//                    successfulUpdates.incrementAndGet();
//                } catch (OptimisticLockingFailureException e) {
//                    optimisticLockExceptionCount.incrementAndGet();
//                    System.out.println("낙관적 락 충돌: " + e.getMessage());
//                }finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await(); // 모든 스레드가 작업을 완료할 때까지 대기
//        executorService.shutdown();
//
//        long endTime = System.currentTimeMillis();
//        long durationInMillis = endTime - startTime;
//        double durationInSeconds = durationInMillis / 1000.0;
//
//        System.out.println("성공한 업데이트 수: " + successfulUpdates.get());
//        System.out.println("실패한 업데이트 수: " + optimisticLockExceptionCount.get());
//        System.out.println("테스트 실행 시간: " + durationInSeconds + "초");
//
//        assertNotNull(successfulUpdates.get());
//
//    }
//*/
//}
