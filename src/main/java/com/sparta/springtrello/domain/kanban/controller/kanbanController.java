package com.sparta.springtrello.domain.kanban.controller;

import com.sparta.springtrello.domain.kanban.dto.request.KanbanSaveRequestDto;
import com.sparta.springtrello.domain.kanban.service.KanbanService;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/{id}/kanbans")
public class kanbanController {

    private final KanbanService kanbanService;

    /**
     칸반 생성
     */
    @PostMapping
    public ResponseEntity<String> createKanban(@AuthenticationPrincipal AuthUser authUser,
                                                       @PathVariable Long id,
                                                       @RequestBody KanbanSaveRequestDto requestDto){
        kanbanService.createKanban(authUser,id,requestDto);
        return ResponseEntity.ok().body("kanban update complete");
    }

    /**
     칸반 수정
     */
    @PutMapping("/{kanbansId}")
    public ResponseEntity<String> updateKanban(@AuthenticationPrincipal AuthUser authUser,
                                               @PathVariable Long id,
                                               @PathVariable Long kanbansId,
                                               @RequestBody KanbanSaveRequestDto requestDto){
        kanbanService.updateKanban(authUser,id,kanbansId,requestDto);
        return ResponseEntity.ok().body("kanban update complete");
    }

    /**
     칸반 순서 변경
     */
    @PutMapping("/{kanbansId}/orders")
    public ResponseEntity<String> updateOrder(@AuthenticationPrincipal AuthUser authUser,
                                              @PathVariable Long id,
                                              @PathVariable Long kanbansId,
                                              @RequestParam Integer newOrder){
        kanbanService.updateOrder(authUser,id,kanbansId,newOrder);
        return ResponseEntity.ok().body("kanban update complete");
    }

    /**
     칸반 삭제 상태변경
     */
    @DeleteMapping("{kanbansId}")
    public ResponseEntity<String> deleteKanban(@AuthenticationPrincipal AuthUser authUser,
                                               @PathVariable Long id,
                                               @PathVariable Long kanbansId,
                                               @RequestBody KanbanSaveRequestDto requestDto){
        kanbanService.deleteKanban(authUser,id,kanbansId,requestDto);
        return ResponseEntity.ok().body("kanban delete complete");

    }
}
