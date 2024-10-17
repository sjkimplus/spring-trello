package com.sparta.springtrello.domain.ticket.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.domain.manager.dto.ManagerRequestDto;
import com.sparta.springtrello.domain.ticket.dto.TicketDetailResponseDto;
import com.sparta.springtrello.domain.ticket.dto.TicketRankingDto;
import com.sparta.springtrello.domain.ticket.dto.TicketRequestDto;
import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import com.sparta.springtrello.domain.ticket.service.TicketService;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    /**
     * Ticket 생성
     * @param authUser : 사용자 정보가 담긴 객체
     * @param requestDto : 티켓 생성에 필요한 정보(제목, 내용, 기한, Kanban Id)
     * @return : 생성된 Ticket의 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<TicketResponseDto>> createTicket(@AuthenticationPrincipal AuthUser authUser,
                                                                          @RequestBody TicketRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponseDto.success(ticketService.createTicket(authUser, requestDto)));
    }

    /**
     * Ticket 조회
     * @param id : 조회하는 Ticket의 Id
     * @return : 조회한 Ticket의 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<TicketDetailResponseDto>> getTicket(@AuthenticationPrincipal AuthUser authUser,
                                                                             @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.success(ticketService.getTicket(authUser,id)));

    }

    /**
     * Ticket 수정
     * @param authUser : 수정하는 사용자 정보가 담긴 객체
     * @param id : 수정하는 Ticket의 Id
     * @param requestDto : 수정할 내용이 담긴 객체(제목, 내용, 기한, Kanban Id)
     * @return : 수정된 Ticket 정보
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<TicketResponseDto>> updateTicket(@AuthenticationPrincipal AuthUser authUser,
                                          @PathVariable Long id,
                                          @RequestBody TicketRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponseDto.success(ticketService.updateTicket(authUser, id, requestDto)));
    }

    /**
     * Ticket 삭제
     * @param authUser : 삭제하는 사용자 정보가 담긴 객체
     * @param id : 삭제하려는 Ticket의 Id
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteTicket(@AuthenticationPrincipal AuthUser authUser,
                                                               @PathVariable Long id) {
        ticketService.deleteTicket(authUser, id);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

    /**
     * 해당 Ticket을 관리(담당)할 관리자 추가
     * @param authUser : 관리자를 추가하는 사용자의 정보가 담긴 객체
     * @param id : 관리자가 추가되는 Ticket의 Id
     * @param requestDto : 추가되는 관리자들의 Id값이 담긴 List
     * @return : 해당 Ticket정보와 댓글들, 관리자들 정보
     */

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponseDto<TicketDetailResponseDto>> addManagerToTicket(@AuthenticationPrincipal AuthUser authUser,
                                                                @PathVariable Long id,
                                                                @RequestBody ManagerRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponseDto.success(ticketService.addManagerToTicket(authUser, id, requestDto)));
    }

//    @GetMapping
//    public ResponseEntity<ApiResponseDto<Page<TicketResponseDto>>> searchTickets(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "5") int size,
//            @RequestParam long workspaceId,
//            @RequestParam(required = false) String ticketKeyword, // 제목 또는 내용이 될수있는 키워드
//            @RequestParam(required = false) String managerName,
//            @RequestParam(required = false) String deadline,
//            @RequestParam(required = false) String boardId)
//    {
//        return ResponseEntity.ok(ApiResponseDto.success(ticketService.searchTickets(page, size, workspaceId, ticketKeyword, managerName, deadline, boardId)));
//    }

  @GetMapping("/ranks")
    public ResponseEntity<ApiResponseDto<List<TicketRankingDto>>> getRanking() {
      return ResponseEntity.ok(ApiResponseDto.success(ticketService.getDailyViewRanking()));
  }
    // 최적화위한 더미데이터 삽입API
    @PostMapping("/pushTickets")
    public ResponseEntity<String> pushTickets() {
        return ResponseEntity.ok(ticketService.pushTickets());
    }



}