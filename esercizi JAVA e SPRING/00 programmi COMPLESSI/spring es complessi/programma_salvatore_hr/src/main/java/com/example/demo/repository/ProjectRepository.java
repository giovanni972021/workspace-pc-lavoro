package com.example.demo.repository;

import com.example.demo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Prende Status 1 OPPURE (Status 0 E data > 01/01/2024)
    @Query("SELECT p FROM Project p WHERE p.status = '1' OR (p.status = '0' AND p.modificationDateTime <= :date) ORDER BY p.id ASC")
    List<Project> findActiveOrRecentlyModified(@Param("date") LocalDateTime date);
}