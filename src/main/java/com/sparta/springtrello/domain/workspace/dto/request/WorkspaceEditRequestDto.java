package com.sparta.springtrello.domain.workspace.dto.request;

import lombok.Getter;

@Getter
public class WorkspaceEditRequestDto {

    private String title;
    private String content;

    public WorkspaceEditRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
