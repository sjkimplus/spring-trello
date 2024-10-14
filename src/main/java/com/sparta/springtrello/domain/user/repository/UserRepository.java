package com.sparta.springtrello.domain.user.repository;

import com.sparta.springtrello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Long> {
}
