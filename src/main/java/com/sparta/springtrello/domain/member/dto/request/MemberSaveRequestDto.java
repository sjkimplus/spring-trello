package com.sparta.springtrello.domain.member.dto.request;

import com.sparta.springtrello.domain.member.entity.MemberRole;
import lombok.Getter;

@Getter
public class MemberSaveRequestDto {

    private MemberRole memberRole;
}
