package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.Project;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private final String ACTIVE_STATUS = "1";

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Tutti i progetti filtrati per status 1
    public List<Project> getAllProjectsSorted() {
        return projectRepository.findByStatusOrderByIdAscParentIdAsc(ACTIVE_STATUS);
    }

    // Progetti Padre filtrati per status 1
    public List<Project> getMainProjects() {
        return projectRepository.findByParentIdIsNullAndStatusOrderByIdAsc(ACTIVE_STATUS);
    }

    // Sotto-progetti filtrati per status 1
    public List<Project> getSubProjects() {
        return projectRepository.findByParentIdIsNotNullAndStatusOrderByIdAscParentIdAsc(ACTIVE_STATUS);
    }
}