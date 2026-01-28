package com.example.demo.repository;

import com.example.demo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Ordina prima per ID e poi per ParentID, entrambi crescenti
    List<Project> findAllByOrderByIdAscParentIdAsc();
}