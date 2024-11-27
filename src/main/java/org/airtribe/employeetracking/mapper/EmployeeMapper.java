package org.airtribe.employeetracking.mapper;

import org.airtribe.employeetracking.dto.EmployeeDTO;
import org.airtribe.employeetracking.entity.Employee;
import org.airtribe.employeetracking.entity.Department;
import org.airtribe.employeetracking.entity.Project;
import org.airtribe.employeetracking.entity.enums.Roles;
import org.airtribe.employeetracking.repository.DepartmentRepository;
import org.airtribe.employeetracking.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    private final DepartmentRepository departmentRepository;

    private final ProjectRepository projectRepository;

    public EmployeeMapper(DepartmentRepository departmentRepository, ProjectRepository projectRepository) {
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
    }


    public Employee toEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();

        employee.setUserId(dto.getUserId());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setDesignation(dto.getDesignation());

        employee.setRole(Roles.EMPLOYEE);

        Department department = departmentRepository.findByDeptName(dto.getDepartmentName())
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + dto.getDepartmentName()));
        employee.setDepartment(department);

        if(dto.getProjectNames() != null) {
            List<Project> projects = dto.getProjectNames().stream()
                    .map(projectName -> projectRepository.findByProjectName(projectName)
                            .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectName)))
                    .collect(Collectors.toList());
            employee.setProjectList(projects);
        }

        return employee;
    }

    public EmployeeDTO toDto(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setUserId(employee.getUserId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setDesignation(employee.getDesignation());

        if(employee.getDepartment() != null)
            employeeDTO.setDepartmentName(employee.getDepartment().getDeptName());
        else
            employeeDTO.setDepartmentName(null);

        return employeeDTO;
    }
}
