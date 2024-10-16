package com.sparta.springtrello.domain.member.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.domain.member.dto.request.MemberSaveRequestDto;
import com.sparta.springtrello.domain.member.dto.response.MemberResponseDto;
import com.sparta.springtrello.domain.member.entity.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.member.service.MemberService;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     멤버 생성
     */
    @PostMapping("/workspaces/{id}/members")
    public ResponseEntity<ApiResponseDto<?>> saveMember(@AuthenticationPrincipal AuthUser authUser,
                                                      @PathVariable Long id,
                                                      @RequestBody MemberSaveRequestDto requestDto){
        memberService.saveMember(authUser,id,requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

    /**
     멤버 리스트 조회
     */
    @GetMapping("/workspaces/{id}/members")
    public ResponseEntity<ApiResponseDto<List<MemberResponseDto>>> getMembers(@PathVariable long id){
        List<MemberResponseDto> members = memberService.getMembers(id);
        return ResponseEntity.ok(ApiResponseDto.success(members));
    }

    /**
     멤버 상태 삭제로 변경
     */
    @DeleteMapping("/workspaces/{id}/members/{memberId}")
    public ResponseEntity<ApiResponseDto<?>> deleteMember(@AuthenticationPrincipal AuthUser authUser,
                                               @PathVariable Long id,
                                               @PathVariable Long memberId){
        memberService.deleteMember(authUser,id,memberId);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }
}
