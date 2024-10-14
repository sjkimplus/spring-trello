package com.sparta.springtrello.domain.member.controller;

import com.sparta.springtrello.domain.member.dto.request.MemberSaveRequestDto;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.member.service.MemberService;
import com.sparta.springtrello.domain.user.entity.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/workspaces/{id}/members/{userId}")
    public ResponseEntity<String> saveMembers(@AuthenticationPrincipal AuthUser authUser,
                                         @PathVariable long id,
                                         @PathVariable long userId,
                                         @RequestBody MemberSaveRequestDto requestDto){
        return ResponseEntity.ok(memberService.saveMember(id,userId,requestDto));
    }
}
