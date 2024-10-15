package com.sparta.springtrello.domain.workspace.dto.response;

import com.sparta.springtrello.domain.workspace.entity.Workspace;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkspaceReadResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public WorkspaceReadResponseDto(Workspace workspace) {
        this.id = workspace.getId();
        this.title = workspace.getTitle();
        this.content = workspace.getContent();
        this.createdAt = workspace.getCreatedAt();
        this.modifiedAt = workspace.getModifiedAt();
    }

    public WorkspaceReadResponseDto(Long id, String title, String title1, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = title1;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
