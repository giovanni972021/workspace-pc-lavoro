package com.example.BaseSpringBoot.model;

import jakarta.persistence.*;

/*
 * Questa classe rappresenta la tabella "projects" del database HR.
 */
@Entity
@Table(name = "projects")
public class Project {

  /*
   * Chiave primaria della tabella.
   * NON mettiamo @GeneratedValue perché la tabella esiste già nel database.
   */
  @Id
  @Column(name = "id")
  private Long id;

  /*
   * IMPORTANTISSIMO:
   * Il nome deve essere IDENTICO a quello nel database: jobOrderid
   */
  @Column(name = "jobOrderid")
  private Long jobOrderId;

  // Costruttore vuoto obbligatorio per JPA
  public Project() {
  }

  // Getter
  public Long getId() {
    return id;
  }

  public Long getJobOrderId() {
    return jobOrderId;
  }

  // Setter
  public void setJobOrderId(Long jobOrderId) {
    this.jobOrderId = jobOrderId;
  }
}
