package org.airtribe.employeetracking.service;

import org.airtribe.employeetracking.dto.ProjectDTO;
import org.airtribe.employeetracking.entity.Project;
import org.airtribe.employeetracking.mapper.ProjectMapper;
import org.airtribe.employeetracking.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMapper projectMapper;

    // Get all projects
    public List<ProjectDTO> getAllProjects() {
        if(projectRepository.findAll() != null)
        {
            return projectRepository.findAll().stream().map(projectMapper::toDto).collect(Collectors.toList());
        }
        return null;
    }

    // Get a specific project by ID
    public Optional<Project> getProjectById(int id) {
        return projectRepository.findById(id);
    }

    // Create a new project
    public Project createProject(ProjectDTO projectDTO) {
        return projectRepository.save(projectMapper.toEntity(projectDTO));
    }

    // Update an existing project
    public Project updateProject(int id, Project updatedProject) {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setProjectName(updatedProject.getProjectName());
                    project.setStartDate(updatedProject.getStartDate());
                    project.setEndDate(updatedProject.getEndDate());
                    project.setProjectStatus(updatedProject.getProjectStatus());
                    project.setDepartment(updatedProject.getDepartment());
                    project.setEmployeeList(updatedProject.getEmployeeList());
                    return projectRepository.save(project);
                })
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id " + id));
    }

    // Delete a project by ID
    public void deleteProject(int id) {
        projectRepository.deleteById(id);
    }
}
