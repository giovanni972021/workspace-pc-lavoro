package com.example.BaseSpringBoot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.BaseSpringBoot.model.Project;
import java.util.List;

/*
 * Interfaccia che comunica direttamente con il database
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

  /*
   * Spring genera automaticamente la query:
   *
   * SELECT * FROM projects WHERE jobOrderId IS NOT NULL
   */
  List<Project> findByJobOrderIdIsNotNull();
}
