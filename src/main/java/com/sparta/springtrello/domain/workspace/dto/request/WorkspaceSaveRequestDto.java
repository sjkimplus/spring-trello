package com.sparta.springtrello.domain.workspace.dto.request;

import lombok.Getter;

@Getter
public class WorkspaceSaveRequestDto {

    private String title;
    private String content;

    public WorkspaceSaveRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
