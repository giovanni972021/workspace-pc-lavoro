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

    // --- LOGICA UTENTI ---

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // --- LOGICA PROGETTI ---

    // Ritorna tutti i progetti ordinati per ID e ParentID
    public List<Project> getAllProjectsSorted() {
        return projectRepository.findAllByOrderByIdAscParentIdAsc();
    }

    // Ritorna solo i progetti "Padre" (dove parentId è NULL)
    public List<Project> getMainProjects() {
        return projectRepository.findByParentIdIsNullOrderByIdAsc();
    }

    // Ritorna solo i "Sotto-progetti" (dove parentId NON è NULL)
    public List<Project> getSubProjects() {
        return projectRepository.findByParentIdIsNotNullOrderByIdAscParentIdAsc();
    }
}