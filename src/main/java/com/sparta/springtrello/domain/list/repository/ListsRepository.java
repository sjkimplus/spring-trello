package com.sparta.springtrello.domain.list.repository;

import com.sparta.springtrello.domain.list.entity.Lists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListsRepository extends JpaRepository<Lists, Long> {
}
