package com.sparta.springtrello.domain.ticket.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.domain.ticket.dto.TicketRequestDto;
import com.sparta.springtrello.domain.ticket.dto.TicketUpdateDto;
import com.sparta.springtrello.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ApiResponseDto<?> createTicket(@AuthenticationPrincipal SignUser signUser,
                                          @RequestBody TicketRequestDto requestDto) {
        return ApiResponseDto<>(ApiResponseDto.success(ticketService.createTicket(signUser, requestDto)), "성공");
    }

    @GetMapping("/{id}")
    public ApiResponseDto<?> getTicket(@PathVariable Long id) {
        return ApiResponseDto<>(ApiResponseDto.success(ticketService.getTicket(id)), "성공");
    }

    @PutMapping("/{id}")
    public ApiResponseDto<?> updateTicket(@AuthenticationPrincipal SignUser signUser,
                                          @PathVariable Long id,
                                          @RequestBody TicketUpdateDto requestDto) {
        return ApiResponseDto<>(ApiResponseDto.success(ticketService.updateTicket(signUser, id, requestDto)), "성공");
    }

    @DeleteMapping("/{id}")
    public ApiResponseDto<?> deleteTicket(@AuthenticationPrincipal SignUser signUser,
                                          @PathVariable Long id) {
        return ApiResponseDto<>(ApiResponseDto.success(ticketService.deleteTicket(signUser, id)), "성공");
    }

}