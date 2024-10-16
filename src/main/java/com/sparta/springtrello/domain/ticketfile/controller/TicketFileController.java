package com.sparta.springtrello.domain.ticketfile.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.domain.ticketfile.dto.responseDto.TicketFileResponseDto;
import com.sparta.springtrello.domain.ticketfile.service.TicketFileService;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketFileController {

    private final TicketFileService ticketFileService;

    // 파일 추가
    @PostMapping("/{id}/file")
    public ResponseEntity<ApiResponseDto<TicketFileResponseDto>> addFile(@AuthenticationPrincipal AuthUser authUser,
                                                                         @PathVariable Long id,
                                                                         @RequestPart MultipartFile file) throws IOException {
        TicketFileResponseDto responseDto = ticketFileService.addFile(authUser, id, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(responseDto));
    }

    // 파일 수정
    @PutMapping("/file/{id}")
    public ResponseEntity<ApiResponseDto<TicketFileResponseDto>> updateFile(@AuthenticationPrincipal AuthUser authUser,
                                                                            @PathVariable Long id,
                                                                            @RequestPart MultipartFile newFile) throws IOException {
        TicketFileResponseDto responseDto = ticketFileService.updateFile(authUser, id, newFile);
        return ResponseEntity.ok(ApiResponseDto.success(responseDto));
    }

    // 파일 삭제
    @DeleteMapping("/file/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteFile(@AuthenticationPrincipal AuthUser authUser,
                                           @PathVariable Long id) {
        ticketFileService.deleteFile(authUser, id);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

}