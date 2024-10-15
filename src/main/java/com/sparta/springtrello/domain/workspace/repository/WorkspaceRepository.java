package com.sparta.springtrello.domain.workspace.repository;

import com.sparta.springtrello.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

}
