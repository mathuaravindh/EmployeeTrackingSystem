package org.airtribe.employeetracking.controller;

import org.airtribe.employeetracking.dto.ProjectDTO;
import org.airtribe.employeetracking.entity.Employee;
import org.airtribe.employeetracking.entity.Project;
import org.airtribe.employeetracking.service.EmployeeService;
import org.airtribe.employeetracking.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EmployeeService employeeService;

    // Get all projects
    @GetMapping("/projects")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public List<ProjectDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    // Get a project by ID
    @GetMapping("/projects/{id}")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public ResponseEntity<Project> getProjectById(@PathVariable int id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new project
    @PostMapping("/projects")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public Project createProject(@RequestBody ProjectDTO projectDTO) {
        return projectService.createProject(projectDTO);
    }

    // Update an existing project
    @PutMapping("/projects/{id}")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public ResponseEntity<Project> updateProject(
            @PathVariable int id,
            @RequestBody Project updatedProject
    ) {
        try {
            return ResponseEntity.ok(projectService.updateProject(id, updatedProject));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a project
    @DeleteMapping("/projects/{id}")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public ResponseEntity<Void> deleteProject(@PathVariable int id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projects/{projectId}/employees")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public List<Employee> getEmployeesByProject(@PathVariable int projectId) {
        return employeeService.getEmployeesByProject(projectId);
    }
}
