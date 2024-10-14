package com.sparta.springtrello.domain.member.repository;

import com.sparta.springtrello.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
