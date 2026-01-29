package com.example.demo.dto;

import java.time.LocalDateTime;

public class ProjectExportDto {

    private Long id;
    private Long parentId;
    private String name;
    private String code;
    private String status;
    private LocalDateTime creationDateTime;
    private LocalDateTime modificationDateTime;

    public ProjectExportDto(Long id, Long parentId, String name, String code, String status,
            LocalDateTime creationDateTime, LocalDateTime modificationDateTime) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.code = code;
        this.status = status;
        this.creationDateTime = creationDateTime;
        this.modificationDateTime = modificationDateTime;
    }

    // getter
    public Long getId() {
        return id;
    }

    public Long getParentId() {
        return parentId;
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
}
