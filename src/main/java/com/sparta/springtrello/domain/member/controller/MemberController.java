package com.sparta.springtrello.domain.member.controller;

import com.sparta.springtrello.domain.member.dto.request.MemberSaveRequestDto;
import com.sparta.springtrello.domain.member.dto.response.MemberResponseDto;
import com.sparta.springtrello.domain.member.service.MemberService;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.slack.SlackNotificationService;
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
    private final SlackNotificationService slackNotificationService;

    /**
     멤버 생성
     */
    @PostMapping("/workspaces/{id}/members/{userId}")
    public ResponseEntity<String> saveMember(@AuthenticationPrincipal AuthUser authUser,
                                         @PathVariable Long id,
                                         @PathVariable Long userId,
                                         @RequestBody MemberSaveRequestDto requestDto){
        memberService.saveMember(authUser,id,userId,requestDto);
        // Slack으로 알림 전송
        String slackMessage = String.format("Workspace ID: %d, User ID: %d - 새로운 맴버가 등록되었습니다", id, userId);
        slackNotificationService.sendSlackMessage(slackMessage);
        return ResponseEntity.ok().body(HttpStatus.OK + ", member save complete.");
    }

    /**
     멤버 리스트 조회
     */
    @GetMapping("/workspaces/{id}/members")
    public ResponseEntity<List<MemberResponseDto>> getMembers(@PathVariable long id){
        return ResponseEntity.ok(memberService.getMembers(id));
    }

    /**
     멤버 상태 삭제로 변경
     */
    @DeleteMapping("/workspaces/{id}/members/{memberId}")
    public ResponseEntity<String> deleteMember(@AuthenticationPrincipal AuthUser authUser,
                                               @PathVariable Long id,
                                               @PathVariable Long memberId,
                                               @RequestBody MemberSaveRequestDto requestDto){
        memberService.deleteMember(authUser,id,memberId,requestDto);
        return ResponseEntity.ok().body("member delete success");
    }
}
