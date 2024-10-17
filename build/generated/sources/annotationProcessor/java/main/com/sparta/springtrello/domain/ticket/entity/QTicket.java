package com.sparta.springtrello.domain.ticket.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTicket is a Querydsl query type for Ticket
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTicket extends EntityPathBase<Ticket> {

    private static final long serialVersionUID = 651026636L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTicket ticket = new QTicket("ticket");

    public final com.sparta.springtrello.common.QTimestamped _super = new com.sparta.springtrello.common.QTimestamped(this);

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath deadline = createString("deadline");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.sparta.springtrello.domain.kanban.entity.QKanban kanban;

    public final com.sparta.springtrello.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<com.sparta.springtrello.common.Status> status = createEnum("status", com.sparta.springtrello.common.Status.class);

    public final StringPath title = createString("title");

    public QTicket(String variable) {
        this(Ticket.class, forVariable(variable), INITS);
    }

    public QTicket(Path<? extends Ticket> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTicket(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTicket(PathMetadata metadata, PathInits inits) {
        this(Ticket.class, metadata, inits);
    }

    public QTicket(Class<? extends Ticket> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.kanban = inits.isInitialized("kanban") ? new com.sparta.springtrello.domain.kanban.entity.QKanban(forProperty("kanban"), inits.get("kanban")) : null;
        this.member = inits.isInitialized("member") ? new com.sparta.springtrello.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

