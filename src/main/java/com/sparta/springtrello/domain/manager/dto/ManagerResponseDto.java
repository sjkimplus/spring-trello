package com.sparta.springtrello.domain.manager.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ManagerResponseDto {
    private List<Long> memberList;

    public ManagerResponseDto(List<Long> memberList){
        this.memberList = memberList;
    }
}
