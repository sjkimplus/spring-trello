package com.sparta.springtrello.domain.ticket.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.domain.ticket.dto.TicketRequestDto;
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


}
