package org.airtribe.employeetracking.mapper;

import org.airtribe.employeetracking.dto.ManagerDTO;
import org.airtribe.employeetracking.dto.ProjectDTO;
import org.airtribe.employeetracking.entity.Department;
import org.airtribe.employeetracking.entity.Manager;
import org.airtribe.employeetracking.entity.Project;
import org.airtribe.employeetracking.entity.enums.ProjectStatus;
import org.airtribe.employeetracking.entity.enums.Roles;
import org.airtribe.employeetracking.repository.DepartmentRepository;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    private final DepartmentRepository departmentRepository;

    public ProjectMapper(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    public Project toEntity(ProjectDTO dto) {
        if (dto == null) {
            return null;
        }

        Project project = new Project();

        project.setProjectName(dto.getProjectName());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setBudget(dto.getBudget());
        project.setProjectStatus(ProjectStatus.valueOf(dto.getProjectStatus()));

        Department department = departmentRepository.findByDeptName(dto.getDepartmentName())
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + dto.getDepartmentName()));
        project.setDepartment(department);

        return project;
    }

    public ProjectDTO toDto(Project project) {
        if (project == null) {
            return null;
        }

        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setEndDate(project.getEndDate());
        projectDTO.setBudget(project.getBudget());
        projectDTO.setProjectStatus(project.getProjectStatus().name());


        if(project.getDepartment() != null)
            projectDTO.setDepartmentName(project.getDepartment().getDeptName());
        else
            projectDTO.setDepartmentName(null);

        return projectDTO;
    }
}
