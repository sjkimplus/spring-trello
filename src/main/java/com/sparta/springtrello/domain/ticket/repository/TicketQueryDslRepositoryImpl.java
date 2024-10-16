package com.sparta.springtrello.domain.ticket.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springtrello.domain.ticket.dto.TicketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.springtrello.domain.board.entity.QBoard.board;
import static com.sparta.springtrello.domain.kanban.entity.QKanban.kanban;
import static com.sparta.springtrello.domain.ticket.entity.QTicket.ticket;
import static com.sparta.springtrello.domain.member.entity.QMember.member;
import static com.sparta.springtrello.domain.user.entity.QUser.user;
import static com.sparta.springtrello.domain.workspace.entity.QWorkspace.workspace;

@Repository
@RequiredArgsConstructor
public class TicketQueryDslRepositoryImpl implements TicketQueryDslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TicketResponseDto> searchTickets(
            long workspaceId,
            String ticketKeyword,
            String managerName,
            String deadline,
            String boardId,
            Pageable pageable
    ) {
        List<TicketResponseDto> results = queryFactory
                .select(
                        Projections.constructor(
                                TicketResponseDto.class,
                                ticket.title,
                                ticket.contents,
                                ticket.deadline,
                                ticket.kanban.id
                        )
                )
                .from(ticket)
                .leftJoin(ticket.member, member)
                .leftJoin(ticket.kanban, kanban)
                .leftJoin(kanban.board, board)
                .leftJoin(board.workspace, workspace)

                .where(
                        sameWorkspace(workspaceId),
                        titleOrContentContains(ticketKeyword),
                        dueAt(deadline),
                        managerName(managerName)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(ticket.id.desc())
                .fetch();

        Long totalCount = queryFactory
                .select(Wildcard.count)
                .from(ticket)
                .where(
                        sameWorkspace(workspaceId),
                        sameBoard(boardId),
                        titleOrContentContains(ticketKeyword),
                        dueAt(deadline),
                        managerName(managerName)
                ).fetchOne();

        return new PageImpl<>(results, pageable, totalCount);
    }

    private BooleanExpression sameWorkspace(long id) {
        // tickets that are in my current workspace
        return ticket.member.workspace.id.eq(id);
    }
    private BooleanExpression titleOrContentContains(String keyword) {
        if (keyword == null) return null;
        return ticket.title.containsIgnoreCase(keyword)
                .or(ticket.contents.containsIgnoreCase(keyword));
    }

    private BooleanExpression dueAt(String deadline) {
        return deadline != null ? ticket.deadline.eq(deadline) : null;
    }

    private BooleanExpression sameBoard(String id) {

        return id != null ? ticket.kanban.board.id.eq(Long.valueOf(id)) : null;
    }

    private BooleanExpression managerName(String managerName) {
        return managerName != null ? member.user.name.containsIgnoreCase(managerName) : null;
    }
}
