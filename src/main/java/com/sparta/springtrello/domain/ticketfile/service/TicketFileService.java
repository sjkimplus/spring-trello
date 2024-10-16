package com.sparta.springtrello.domain.ticketfile.service;

import com.sparta.springtrello.config.s3.FileValidator;
import com.sparta.springtrello.config.s3.S3Uploader;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.member.entity.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.ticket.entity.Ticket;
import com.sparta.springtrello.domain.ticket.repository.TicketRepository;
import com.sparta.springtrello.domain.ticketfile.dto.requestDto.TicketFileRequestDto;
import com.sparta.springtrello.domain.ticketfile.dto.responseDto.TicketFileResponseDto;
import com.sparta.springtrello.domain.ticketfile.entity.TicketFile;
import com.sparta.springtrello.domain.ticketfile.repository.TicketFileRepository;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketFileService {

    private final TicketFileRepository ticketFileRepository;
    private final TicketRepository ticketRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;
    private final UserService userService;

    // 파일 추가
    public TicketFileResponseDto addFile(AuthUser authUser, Long id, MultipartFile file) throws IOException {

        Long userId = authUser.getId();
        userService.checkUser(userId);

        // ticket을 등록하는 멤버 찾기
        Member member = memberRepository.findById(authUser.getId()).orElseThrow(() ->
                new RuntimeException("User not found"));

        // ticket을 등록하려는 유저의 role이 CREATOR인지 확인
        if (!member.getMemberRole().equals(MemberRole.ROLE_CREATOR)) {
            throw new RuntimeException("Only creators can create tickets.");
        }

        Ticket ticket = ticketRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("티켓을 찾을 수 없습니다."));

        // 파일 검증
        FileValidator.validateFile(file);

        // 파일 S3 업로드
        String fileUrl = s3Uploader.uploadFile(file);

        TicketFile ticketFile = new TicketFile(ticket, fileUrl);
        ticketFileRepository.save(ticketFile);

        return new TicketFileResponseDto(ticketFile.getId(), ticket.getId(), ticketFile.getFileUrl());
    }

    // 파일 수정
    public TicketFileResponseDto updateFile(AuthUser authUser, Long id, MultipartFile newFile) throws IOException {

        Long userId = authUser.getId();
        userService.checkUser(userId);

        TicketFile ticketFile = ticketFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));

        // 새 파일 검증
        FileValidator.validateFile(newFile);

        // 기존 파일 삭제
        ticketFileRepository.delete(ticketFile);

        // 새 파일 S3 업로드
        String newFileUrl = s3Uploader.uploadFile(newFile);

        // 새 TicketFile 생성 및 저장
        TicketFile newTicketFile = new TicketFile(ticketFile.getTicket(), newFileUrl);
        ticketFileRepository.save(newTicketFile);

        return new TicketFileResponseDto(newTicketFile.getId(), newTicketFile.getTicket().getId(), newTicketFile.getFileUrl());
    }

    // 파일 삭제
    public void deleteFile(AuthUser authUser, Long id) {

        Long userId = authUser.getId();
        userService.checkUser(userId);

        TicketFile ticketFile = ticketFileRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("파일을 찾을 수 없습니다."));

        ticketFileRepository.delete(ticketFile);
    }

}