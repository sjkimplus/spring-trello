package com.sparta.springtrello.domain.board.entity;

import com.sparta.springtrello.common.Status;
import com.sparta.springtrello.common.Timestamped;
import com.sparta.springtrello.domain.user.entity.User;
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

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Board(User user, Workspace workspace, String title, String background, String image) {
        this.user = user;
        this.workspace = workspace;
        this.title = title;
        this.background = background;
        this.image = image;
        this.status = Status.ACTIVATED;
    }

    public void updateBoard(String title, String background, String image) {
        this.title = title;
        this.background = background;
        this.image = image;
    }

    public void deleteBoard() {
        this.status = Status.DELETED;
    }
}
