package com.example.demo.controller;

import com.example.demo.dto.ProjectTreeDto;
import com.example.demo.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectApiController {

    private final UserService userService;

    public ProjectApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/projects/tree")
    public List<ProjectTreeDto> getProjectTree() {
        return userService.getProjectTree();
    }

    @GetMapping("/projects/download")
    public ResponseEntity<List<ProjectTreeDto>> downloadTree() {
        List<ProjectTreeDto> tree = userService.getProjectTree();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=progetti_filtrati.json")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(tree);
    }
}