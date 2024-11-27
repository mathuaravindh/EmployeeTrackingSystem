package org.airtribe.employeetracking.service;

import org.airtribe.employeetracking.dto.ProjectDTO;
import org.airtribe.employeetracking.entity.Department;
import org.airtribe.employeetracking.entity.Project;
import org.airtribe.employeetracking.mapper.ProjectMapper;
import org.airtribe.employeetracking.repository.DepartmentRepository;
import org.airtribe.employeetracking.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    // Get all departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Get a specific department by ID
    public Optional<Department> getDepartmentById(int id) {
        return departmentRepository.findById(id);
    }

    // Create a new department
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    // Update an existing department
    public Department updateDepartment(int id, Department updatedDepartment) {
        return departmentRepository.findById(id)
                .map(department -> {
                    department.setDeptName(updatedDepartment.getDeptName());
                    department.setProjectList(updatedDepartment.getProjectList());
                    return departmentRepository.save(department);
                })
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id " + id));
    }

    // Delete a department by ID
    public void deleteDepartment(int id) {
        departmentRepository.deleteById(id);
    }

    public List<ProjectDTO> getDepartmentProjectsById(int deptId) {
        if (departmentRepository.findById(deptId).isPresent()) {
            Department department = departmentRepository.findById(deptId).get();
            return department.getProjectList().stream().map(projectMapper::toDto).collect(Collectors.toList());
        }
        return null;
    }

    public Double getTotalBudgetForDepartment(int deptId) {
        return projectRepository.getTotalBudgetByDepartment(deptId);
    }
}
