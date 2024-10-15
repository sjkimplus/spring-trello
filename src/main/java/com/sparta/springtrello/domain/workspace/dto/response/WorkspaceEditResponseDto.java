package com.sparta.springtrello.domain.workspace.dto.response;

import com.sparta.springtrello.domain.workspace.entity.Workspace;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkspaceEditResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime modifiedAt;

    public WorkspaceEditResponseDto(Workspace workspace) {
        this.id = workspace.getId();
        this.title = workspace.getTitle();
        this.content = workspace.getContent();
        this.modifiedAt = workspace.getModifiedAt();
    }
}
