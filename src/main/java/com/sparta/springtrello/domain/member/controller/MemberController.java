package com.sparta.springtrello.domain.member.controller;

import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    public ResponseEntity<?> saveMembers(){

    }
}
