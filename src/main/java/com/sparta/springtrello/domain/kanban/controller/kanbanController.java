package com.sparta.springtrello.domain.kanban.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.domain.kanban.dto.request.KanbanSaveRequestDto;
import com.sparta.springtrello.domain.kanban.dto.response.KanbanResponseDto;
import com.sparta.springtrello.domain.kanban.entity.Kanban;
import com.sparta.springtrello.domain.kanban.service.KanbanService;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces/{id}/board/{boardId}/kanbans")
public class kanbanController {

    private final KanbanService kanbanService;

    /**
     칸반 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<?>> createKanban(@AuthenticationPrincipal AuthUser authUser,
                                                       @PathVariable Long id,
                                                       @PathVariable Long boardId,
                                                       @RequestBody KanbanSaveRequestDto requestDto){
        kanbanService.createKanban(authUser,id,boardId,requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

    /**
     칸반 수정
     */
    @PutMapping("/{kanbansId}")
    public ResponseEntity<ApiResponseDto<?>> updateKanban(@AuthenticationPrincipal AuthUser authUser,
                                               @PathVariable Long id,
                                               @PathVariable Long kanbansId,
                                               @RequestBody KanbanSaveRequestDto requestDto){
        kanbanService.updateKanban(authUser,id,kanbansId,requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

    /**
     칸반 순서 변경
     */
    @PutMapping("/{kanbansId}/orders")
    public ResponseEntity<ApiResponseDto<?>> updateOrder(@AuthenticationPrincipal AuthUser authUser,
                                              @PathVariable Long id,
                                              @PathVariable Long kanbansId,
                                              @RequestParam Integer newOrder){
        kanbanService.updateOrder(authUser,id,kanbansId,newOrder);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

    /**
     칸반 삭제 상태변경
     */
    @DeleteMapping("{kanbansId}")
    public ResponseEntity<ApiResponseDto<?>> deleteKanban(@AuthenticationPrincipal AuthUser authUser,
                                               @PathVariable Long id,
                                               @PathVariable Long kanbansId,
                                               @RequestBody KanbanSaveRequestDto requestDto){
        kanbanService.deleteKanban(authUser,id,kanbansId,requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

    @GetMapping()
    public ResponseEntity<List<KanbanResponseDto>> getKanbans(@PathVariable Long id,
                                                              @PathVariable Long boardId){
        return ResponseEntity.ok(kanbanService.getKanbans(id,boardId));
    }
}
