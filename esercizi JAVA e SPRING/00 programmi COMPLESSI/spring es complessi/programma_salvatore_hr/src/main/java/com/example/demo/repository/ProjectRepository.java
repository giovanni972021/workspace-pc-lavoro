package com.example.demo.repository;

import com.example.demo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Tutti i progetti con status = 1
    List<Project> findByStatusOrderByIdAscParentIdAsc(String status);

    // Solo progetti PADRE con status = 1
    List<Project> findByParentIdIsNullAndStatusOrderByIdAsc(String status);

    // Solo SOTTO-PROGETTI con status = 1
    List<Project> findByParentIdIsNotNullAndStatusOrderByIdAscParentIdAsc(String status);
}