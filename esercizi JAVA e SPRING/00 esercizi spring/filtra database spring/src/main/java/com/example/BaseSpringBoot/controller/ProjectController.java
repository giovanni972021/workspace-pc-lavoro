package com.example.BaseSpringBoot.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.BaseSpringBoot.model.Project;
import com.example.BaseSpringBoot.service.ProjectService;

/*
 * Espone l'endpoint REST per i progetti
 */
@RestController
@RequestMapping("/api")
public class ProjectController {

  private final ProjectService projectService;

  /*
   * Dependency Injection automatica
   */
  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  /*
   * Endpoint:
   * http://localhost:8080/api/projects/with-joborder
   *
   * Restituisce solo i progetti dove jobOrderId NON è NULL
   */
  @GetMapping("/projects/with-joborder")
  public List<Project> getProjects() {
    return projectService.getProjectsConJobOrder();
  }
}
