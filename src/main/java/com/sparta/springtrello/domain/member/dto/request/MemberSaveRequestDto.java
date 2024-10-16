package com.sparta.springtrello.domain.member.dto.request;

import com.sparta.springtrello.domain.member.entity.MemberRole;
import lombok.Getter;

import javax.swing.*;

@Getter
public class MemberSaveRequestDto {
    private String email;
    private MemberRole memberRole;
}
