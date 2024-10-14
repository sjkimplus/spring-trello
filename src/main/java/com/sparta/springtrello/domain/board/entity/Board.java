package com.sparta.springtrello.domain.board.entity;

import com.sparta.springtrello.common.Status;
import com.sparta.springtrello.common.Timestamped;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    //배경색
    private String background;

    //배경이미지
    private String image;

    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;
}
