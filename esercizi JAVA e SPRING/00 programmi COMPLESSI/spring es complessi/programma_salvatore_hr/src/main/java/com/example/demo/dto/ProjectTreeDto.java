package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectTreeDto {
    private Long id;
    private String name;
    private String code;
    private String status;
    private LocalDateTime creationDateTime;
    private LocalDateTime modificationDateTime;
    private List<ProjectTreeDto> children;

    public ProjectTreeDto(Long id, String name, String code, String status,
            LocalDateTime creationDateTime,
            LocalDateTime modificationDateTime) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.status = status;
        this.creationDateTime = creationDateTime;
        this.modificationDateTime = modificationDateTime;
        this.children = new ArrayList<>();
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public LocalDateTime getModificationDateTime() {
        return modificationDateTime;
    }

    public List<ProjectTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<ProjectTreeDto> children) {
        this.children = children;
    }
}