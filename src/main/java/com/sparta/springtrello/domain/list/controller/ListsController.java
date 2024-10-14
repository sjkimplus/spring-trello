package com.sparta.springtrello.domain.list.controller;

import com.sparta.springtrello.domain.list.service.ListsService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ListsController {

    private final ListsService listsService;
}
