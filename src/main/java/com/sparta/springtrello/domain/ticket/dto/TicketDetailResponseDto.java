package com.sparta.springtrello.domain.ticket.dto;

import com.sparta.springtrello.domain.comment.dto.response.CommentSaveResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class TicketDetailResponseDto {
    private String title;
    private String contents;
    private String deadline;
    private Long kanbanId;
    private List<CommentSaveResponseDto> commentList;
    private List<Long> memberList;

    public TicketDetailResponseDto(String title, String contents, String deadline, Long kanbanId, List<CommentSaveResponseDto> commentList, List<Long> memberList ) {
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
        this.kanbanId = kanbanId;
        this.commentList = commentList;
        this.memberList = memberList;
    }

}
