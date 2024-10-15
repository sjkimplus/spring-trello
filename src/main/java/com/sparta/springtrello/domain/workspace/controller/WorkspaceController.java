package com.sparta.springtrello.domain.workspace.controller;

import com.sparta.springtrello.common.dto.ApiResponseDto;
import com.sparta.springtrello.domain.user.dto.AuthUser;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceEditRequestDto;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceSaveRequestDto;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceEditResponseDto;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceReadResponseDto;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceSaveResponseDto;
import com.sparta.springtrello.domain.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    // 워크스페이스 생성
    @PostMapping()
    public ResponseEntity<ApiResponseDto<WorkspaceSaveResponseDto>> createWorkspace(@AuthenticationPrincipal AuthUser authUser,
                                                                                    @RequestBody WorkspaceSaveRequestDto workspaceSaveRequestDto) {
        WorkspaceSaveResponseDto saveResponseDto = workspaceService.createWorkspace(authUser, workspaceSaveRequestDto);
        return ResponseEntity.ok(ApiResponseDto.success(saveResponseDto));
    }

    // 워크스페이스 조회
    @GetMapping()
    public ResponseEntity<ApiResponseDto<List<WorkspaceReadResponseDto>>> readWorkspace(@AuthenticationPrincipal AuthUser authUser) {
        List<WorkspaceReadResponseDto> readResponseDto = workspaceService.readWorkspace(authUser);
        return ResponseEntity.ok(ApiResponseDto.success(readResponseDto));
    }

    // 워크스페이스 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<WorkspaceEditResponseDto>> editWorkspace(@AuthenticationPrincipal AuthUser authUser,
                                                                                  @RequestBody WorkspaceEditRequestDto workspaceEditRequestDto,
                                                                                  @PathVariable Long id) {
        WorkspaceEditResponseDto editResponseDto = workspaceService.editWorkspace(authUser, workspaceEditRequestDto, id);
        return ResponseEntity.ok(ApiResponseDto.success(editResponseDto));
    }

    // 워크스페이스 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteWorkspace(@PathVariable Long id,
                                                                @AuthenticationPrincipal AuthUser authUser) {
        workspaceService.deleteWorkspace(id, authUser);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

}
