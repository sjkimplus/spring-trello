package com.sparta.springtrello.domain.list.service;

import com.sparta.springtrello.domain.list.repository.ListsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListsService {

    private final ListsRepository listsRepository;
}
