package com.sparta.springtrello.domain.ticket.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.domain.ticket.dto.TicketRequestDto;
import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import com.sparta.springtrello.domain.ticket.dto.TicketUpdateDto;
import com.sparta.springtrello.domain.ticket.service.TicketService;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<TicketResponseDto>> createTicket(@AuthenticationPrincipal AuthUser authUser,
                                                                          @RequestBody TicketRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponseDto.success(ticketService.createTicket(authUser, requestDto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<TicketResponseDto>> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.success(ticketService.getTicket(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<TicketResponseDto>> updateTicket(@AuthenticationPrincipal AuthUser authUser,
                                          @PathVariable Long id,
                                          @RequestBody TicketUpdateDto requestDto) {
        return ResponseEntity.ok(ApiResponseDto.success(ticketService.updateTicket(authUser, id, requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteTicket(@AuthenticationPrincipal AuthUser authUser,
                                                               @PathVariable Long id) {
        ticketService.deleteTicket(authUser, id);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<TicketResponseDto>>> searchTickets(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam long workspaceId,
            @RequestParam(required = false) String ticketTitle, // 제목 또는 내용이 될수있는 키워드
            @RequestParam(required = false) String ticketContents,
            @RequestParam(required = false) String managerName,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) String boardId)
    {
        return ResponseEntity.ok(ApiResponseDto.success(ticketService.searchTickets(page, size, workspaceId, ticketTitle, ticketContents, managerName, deadline, boardId)));
    }

    // 최적화위한 더미데이터 삽입API
    @PostMapping("/pushTickets")
    public ResponseEntity<String> pushTickets() {
     return ResponseEntity.ok(ticketService.pushTickets());
    }
}