package com.sparta.springtrello.domain.workspace.dto.response;

import com.sparta.springtrello.domain.workspace.entity.Workspace;
import lombok.Getter;

@Getter
public class WorkspaceSaveResponseDto {

    private Long id;
    private String title;
    private String content;

    public WorkspaceSaveResponseDto(Workspace workspace) {
        this.id = workspace.getId();
        this.title = workspace.getTitle();
        this.content = workspace.getContent();
    }
}
