package com.example.BaseSpringBoot.model;

import jakarta.persistence.*;

/*
 * Mappa la tabella "projects" del database hr
 */
@Entity
@Table(name = "projects")
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /*
   * Colonna del database:
   * jobOrderId
   */
  @Column(name = "jobOrderId")
  private Long jobOrderId;

  /*
   * Costruttore vuoto obbligatorio per JPA
   */
  public Project() {
  }

  // ===== GETTER =====

  public Long getId() {
    return id;
  }

  public Long getJobOrderId() {
    return jobOrderId;
  }

  // ===== SETTER =====

  public void setJobOrderId(Long jobOrderId) {
    this.jobOrderId = jobOrderId;
  }
}
