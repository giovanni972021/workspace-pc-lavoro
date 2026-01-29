package com.example.demo.controller;

import com.example.demo.dto.ProjectTreeDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectApiController {

    @Autowired
    private UserService userService;

    @GetMapping("/projects/tree")
    public List<ProjectTreeDto> getProjectTree() {
        return userService.getProjectTree();
    }
}