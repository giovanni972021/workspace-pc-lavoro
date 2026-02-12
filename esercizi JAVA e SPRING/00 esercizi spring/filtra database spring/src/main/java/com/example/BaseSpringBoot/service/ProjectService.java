package com.example.BaseSpringBoot.service;

import org.springframework.stereotype.Service;
import java.util.List;
import com.example.BaseSpringBoot.model.Project;
import com.example.BaseSpringBoot.repository.ProjectRepository;

/*
 * Contiene la logica applicativa
 */
@Service
public class ProjectService {

  private final ProjectRepository projectRepository;

  public ProjectService(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  /*
   * Recupera i progetti con jobOrderId non nullo
   */
  public List<Project> getProjectsConJobOrder() {
    return projectRepository.findByJobOrderIdIsNotNull();
  }
}
