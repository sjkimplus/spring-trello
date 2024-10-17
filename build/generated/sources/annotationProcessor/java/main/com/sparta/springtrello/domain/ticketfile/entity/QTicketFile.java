package com.sparta.springtrello.domain.ticketfile.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTicketFile is a Querydsl query type for TicketFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTicketFile extends EntityPathBase<TicketFile> {

    private static final long serialVersionUID = 116836708L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTicketFile ticketFile = new QTicketFile("ticketFile");

    public final StringPath fileUrl = createString("fileUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.sparta.springtrello.domain.ticket.entity.QTicket ticket;

    public QTicketFile(String variable) {
        this(TicketFile.class, forVariable(variable), INITS);
    }

    public QTicketFile(Path<? extends TicketFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTicketFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTicketFile(PathMetadata metadata, PathInits inits) {
        this(TicketFile.class, metadata, inits);
    }

    public QTicketFile(Class<? extends TicketFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ticket = inits.isInitialized("ticket") ? new com.sparta.springtrello.domain.ticket.entity.QTicket(forProperty("ticket"), inits.get("ticket")) : null;
    }

}

