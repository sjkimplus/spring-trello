package com.sparta.springtrello.domain.kanban.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QKanban is a Querydsl query type for Kanban
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKanban extends EntityPathBase<Kanban> {

    private static final long serialVersionUID = 2145567202L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QKanban kanban = new QKanban("kanban");

    public final com.sparta.springtrello.common.QTimestamped _super = new com.sparta.springtrello.common.QTimestamped(this);

    public final com.sparta.springtrello.domain.board.entity.QBoard board;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> kanbanOrder = createNumber("kanbanOrder", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<com.sparta.springtrello.common.Status> status = createEnum("status", com.sparta.springtrello.common.Status.class);

    public final StringPath title = createString("title");

    public QKanban(String variable) {
        this(Kanban.class, forVariable(variable), INITS);
    }

    public QKanban(Path<? extends Kanban> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QKanban(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QKanban(PathMetadata metadata, PathInits inits) {
        this(Kanban.class, metadata, inits);
    }

    public QKanban(Class<? extends Kanban> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.sparta.springtrello.domain.board.entity.QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

