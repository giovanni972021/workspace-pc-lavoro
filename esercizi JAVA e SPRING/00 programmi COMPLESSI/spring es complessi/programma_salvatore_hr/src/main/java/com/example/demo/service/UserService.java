package com.example.demo.service;

import com.example.demo.dto.ProjectTreeDto;
import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Value("${project.filter.date}")
    private String filterDateStr;

    public UserService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<Project> getAllProjectsSorted() {
        LocalDateTime limitDate = LocalDateTime.parse(filterDateStr);
        return projectRepository.findActiveOrRecentlyModified(limitDate);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<ProjectTreeDto> getProjectTree() {
        List<Project> allProjects = getAllProjectsSorted();

        Map<Long, ProjectTreeDto> dtoMap = allProjects.stream()
                .collect(Collectors.toMap(
                        Project::getId,
                        p -> new ProjectTreeDto(p.getId(), p.getParentId(), p.getName(), p.getCode(),
                                p.getStatus(), p.getCreationDateTime(), p.getModificationDateTime())));

        List<ProjectTreeDto> rootCommesse = new ArrayList<>();
        for (Project p : allProjects) {
            ProjectTreeDto currentDto = dtoMap.get(p.getId());
            if (p.getParentId() == null) {
                rootCommesse.add(currentDto);
            } else {
                ProjectTreeDto padre = dtoMap.get(p.getParentId());
                if (padre != null) {
                    padre.getAttivita().add(currentDto);
                }
            }
        }
        return rootCommesse;
    }
}