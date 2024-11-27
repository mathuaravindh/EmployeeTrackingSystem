package org.airtribe.employeetracking.controller;

import org.airtribe.employeetracking.dto.ProjectDTO;
import org.airtribe.employeetracking.entity.Department;
import org.airtribe.employeetracking.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // Get all departments
    @GetMapping("/departments")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    // Get a department by ID
    @GetMapping("/departments/{id}")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public ResponseEntity<Department> getDepartmentById(@PathVariable int id) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new department
    @PostMapping("/departments")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public Department createDepartment(@RequestBody Department department) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Controller User: " + authentication.getName());
        System.out.println("Controller Authorities: " + authentication.getAuthorities());
        return departmentService.createDepartment(department);
    }

    // Update an existing department
    @PutMapping("/departments/{id}")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable int id,
            @RequestBody Department updatedDepartment
    ) {
        try {
            return ResponseEntity.ok(departmentService.updateDepartment(id, updatedDepartment));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a department
    @DeleteMapping("/departments/{id}")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public ResponseEntity<Void> deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/departments/{id}/projects")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public List<ProjectDTO> getDepartmentProjectsById(@PathVariable int id) {
        return departmentService.getDepartmentProjectsById(id);
    }

    @GetMapping("/departments/{deptId}/budget")
    @PreAuthorize("@securityService.hasAccessToManagerEndpoint(authentication)")
    public Double getTotalBudget(@PathVariable int deptId) {
        return departmentService.getTotalBudgetForDepartment(deptId);
    }
}
