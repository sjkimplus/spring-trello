package com.sparta.springtrello.domain.manager.dto;

import lombok.Getter;

import java.util.List;
@Getter
public class ManagerRequestDto {
    private List<Long> memberList;
}
