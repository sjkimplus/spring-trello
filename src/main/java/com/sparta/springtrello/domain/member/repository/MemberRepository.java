package com.sparta.springtrello.domain.member.repository;

import com.sparta.springtrello.domain.member.entity.Member;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByWorkspaceAndUser(Workspace workspace, User user);

    @Query("SELECT m from Member m join fetch m.workspace where m.workspace.id = :workspaceId")
    List<Member> findByWorkspaceId(@Param("workspaceId") Long id);

    Optional<Member> findByWorkspaceAndId(Workspace workspace, Long memberId);

    @Query("SELECT m FROM Member m WHERE m.user.id = :userId")
    Optional<Member> findByUserId(@Param("userId") Long userId);
}
