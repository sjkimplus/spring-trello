package com.sparta.springtrello.domain.user.repository;

import com.sparta.springtrello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //    Optional<User> findById(long id); // 필요 없는 경우 나중에 삭제
    Optional<User> findByEmail(String email);

}
