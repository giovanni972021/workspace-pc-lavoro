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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Metodo per ottenere i progetti ordinati
    public List<Project> getAllProjectsSorted() {
        return projectRepository.findAllByOrderByIdAscParentIdAsc();
    }
}