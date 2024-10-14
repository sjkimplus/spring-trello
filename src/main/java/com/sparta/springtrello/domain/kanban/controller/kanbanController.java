package com.sparta.springtrello.domain.kanban.controller;

import com.sparta.springtrello.domain.kanban.dto.request.KanbanSaveRequestDto;
import com.sparta.springtrello.domain.kanban.service.KanbanService;
import com.sparta.springtrello.domain.user.entity.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/{id}/kanbans")
public class kanbanController {

    private final KanbanService kanbanService;

    @PostMapping
    public ResponseEntity<String> createKanban(@AuthenticationPrincipal AuthUser authUser,
                                               @PathVariable Long id,
                                               @RequestBody KanbanSaveRequestDto requestDto){
        kanbanService.createKanban(authUser,id,requestDto);
        return ResponseEntity.ok().body("kanban create complete");
    }

    @PutMapping("/{kanbansId}")
    public ResponseEntity<String> updateKanban(@AuthenticationPrincipal AuthUser authUser,
                                               @PathVariable Long id,
                                               @PathVariable Long kanbansId,
                                               @RequestBody KanbanSaveRequestDto requestDto){
        kanbanService.updateKanban(authUser,id,kanbansId,requestDto);
        return ResponseEntity.ok().body("kanban update complete");
    }

    @DeleteMapping("{kanbansId}")
    public ResponseEntity<String> deleteKanban(@AuthenticationPrincipal AuthUser authUser,
                                               @PathVariable Long id,
                                               @PathVariable Long kanbansId,
                                               @RequestBody KanbanSaveRequestDto requestDto){
        kanbanService.deleteKanban(authUser,id,kanbansId,requestDto);
        return ResponseEntity.ok().body("kanban delete complete");

    }
}
