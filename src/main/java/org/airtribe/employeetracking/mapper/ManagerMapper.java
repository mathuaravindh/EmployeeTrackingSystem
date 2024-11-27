package org.airtribe.employeetracking.mapper;


import org.airtribe.employeetracking.dto.EmployeeDTO;
import org.airtribe.employeetracking.dto.ManagerDTO;
import org.airtribe.employeetracking.entity.Department;
import org.airtribe.employeetracking.entity.Employee;
import org.airtribe.employeetracking.entity.Manager;
import org.airtribe.employeetracking.entity.Project;
import org.airtribe.employeetracking.entity.enums.Roles;
import org.airtribe.employeetracking.repository.DepartmentRepository;
import org.airtribe.employeetracking.repository.ProjectRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ManagerMapper {

    private final DepartmentRepository departmentRepository;

    public ManagerMapper(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    public Manager toEntity(ManagerDTO dto) {
        if (dto == null) {
            return null;
        }

        Manager manager = new Manager();

        manager.setName(dto.getName());
        manager.setEmail(dto.getEmail());

        manager.setRole(Roles.MANAGER);

        Department department = departmentRepository.findByDeptName(dto.getDepartmentName())
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + dto.getDepartmentName()));
        manager.setDepartment(department);

        return manager;
    }

    public ManagerDTO toDto(Manager manager) {
        if (manager == null) {
            return null;
        }

        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setName(manager.getName());
        managerDTO.setEmail(manager.getEmail());


        if(manager.getDepartment() != null)
            managerDTO.setDepartmentName(manager.getDepartment().getDeptName());
        else
            managerDTO.setDepartmentName(null);

        return managerDTO;
    }
}
