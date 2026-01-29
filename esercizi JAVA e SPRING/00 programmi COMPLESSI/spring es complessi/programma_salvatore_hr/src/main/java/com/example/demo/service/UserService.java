package com.example.demo.service;

import com.example.demo.dto.ProjectTreeDto;
import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<Project> getAllProjectsSorted() {
        return projectRepository.findByStatusOrderByIdAscParentIdAsc(ACTIVE_STATUS);
    }

    public List<Project> getMainProjects() {
        return projectRepository.findByParentIdIsNullAndStatusOrderByIdAsc(ACTIVE_STATUS);
    }

    public List<Project> getSubProjects() {
        return projectRepository.findByParentIdIsNotNullAndStatusOrderByIdAscParentIdAsc(ACTIVE_STATUS);
    }

    // LOGICA JSON UNICO AD ALBERO
    public List<ProjectTreeDto> getProjectTree() {
        List<Project> allProjects = projectRepository.findByStatusOrderByIdAscParentIdAsc(ACTIVE_STATUS);

        // Mappa per associare ID all'oggetto DTO
        Map<Long, ProjectTreeDto> dtoMap = allProjects.stream()
                .collect(Collectors.toMap(
                        Project::getId,
                        p -> new ProjectTreeDto(p.getId(), p.getName(), p.getCode(), p.getStatus(),
                                p.getCreationDateTime(), p.getModificationDateTime())));

        List<ProjectTreeDto> tree = new ArrayList<>();

        for (Project p : allProjects) {
            ProjectTreeDto dto = dtoMap.get(p.getId());
            if (p.getParentId() == null) {
                // Se non ha parent, è una commessa principale
                tree.add(dto);
            } else {
                // Se ha un parent, lo aggiungo alla lista children del padre
                ProjectTreeDto parentDto = dtoMap.get(p.getParentId());
                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                }
            }
        }
        return tree;
    }
}