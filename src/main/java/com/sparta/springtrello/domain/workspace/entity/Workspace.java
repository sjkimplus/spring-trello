package com.sparta.springtrello.domain.workspace.entity;

import com.sparta.springtrello.common.Timestamped;
import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceEditRequestDto;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceSaveRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Workspace extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private String content;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();


    public Workspace(WorkspaceSaveRequestDto workspaceSaveRequestDto) {
        this.title = workspaceSaveRequestDto.getTitle();
        this.content = workspaceSaveRequestDto.getContent();
    }

    public void update(WorkspaceEditRequestDto workspaceEditRequestDto) {
        this.title = workspaceEditRequestDto.getTitle();
        this.content = workspaceEditRequestDto.getContent();
    }
}
