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
   * 
   * metodo che restituisce una lista di elementi di tipo project, quindi ogni
   * elemento della lista conterra tutte le caratteristiche definite nella classe
   * project
   * 
   * findByJobOrderIdIsNotNull nome del metodo
   */
  List<Project> findByJobOrderIdIsNotNull();
}
