package com.sparta.springtrello.domain.workspace.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkspace is a Querydsl query type for Workspace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkspace extends EntityPathBase<Workspace> {

    private static final long serialVersionUID = -1108932678L;

    public static final QWorkspace workspace = new QWorkspace("workspace");

    public final com.sparta.springtrello.common.QTimestamped _super = new com.sparta.springtrello.common.QTimestamped(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.sparta.springtrello.domain.member.entity.Member, com.sparta.springtrello.domain.member.entity.QMember> members = this.<com.sparta.springtrello.domain.member.entity.Member, com.sparta.springtrello.domain.member.entity.QMember>createList("members", com.sparta.springtrello.domain.member.entity.Member.class, com.sparta.springtrello.domain.member.entity.QMember.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath title = createString("title");

    public QWorkspace(String variable) {
        super(Workspace.class, forVariable(variable));
    }

    public QWorkspace(Path<? extends Workspace> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWorkspace(PathMetadata metadata) {
        super(Workspace.class, metadata);
    }

}

