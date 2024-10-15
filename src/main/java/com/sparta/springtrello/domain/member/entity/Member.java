package com.sparta.springtrello.domain.member.entity;

import com.sparta.springtrello.common.entity.Timestamped;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Enumerated(value = EnumType.STRING)
    private MemberRole memberRole;

    public Member (User user,Workspace workspace, MemberRole memberRole){
        this.user = user;
        this.workspace = workspace;
        this.memberRole = memberRole;
    }

    public void deleteMember() {
        this.memberRole = MemberRole.ROLE_DELETE;
    }
}
