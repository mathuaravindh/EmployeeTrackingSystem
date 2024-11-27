package org.airtribe.employeetracking.service;

import org.airtribe.employeetracking.dto.EmployeeDTO;
import org.airtribe.employeetracking.entity.Department;
import org.airtribe.employeetracking.entity.Employee;
import org.airtribe.employeetracking.mapper.EmployeeMapper;
import org.airtribe.employeetracking.repository.DepartmentRepository;
import org.airtribe.employeetracking.repository.EmployeeRepository;
import org.airtribe.employeetracking.repository.specification.EmployeeSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeMapper employeeMapper, DepartmentRepository departmentRepository
                            , EmployeeRepository employeeRepository) {
        this.employeeMapper = employeeMapper;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

public Employee addEmployee(EmployeeDTO employeeDTO)
{
    return employeeRepository.save(employeeMapper.toEntity(employeeDTO));
}

public List<EmployeeDTO> getAllEmployees()
{
    return employeeRepository.findAll().stream().map(employeeMapper::toDto).collect(Collectors.toList());
}

    public Optional<EmployeeDTO> getEmployeeById(int id) {
        return Optional.ofNullable(employeeMapper.toDto(employeeRepository.findById(id).get()));
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(updatedEmployee.getName());
                    employee.setEmail(updatedEmployee.getEmail());
                    employee.setDesignation(updatedEmployee.getDesignation());
                    employee.setDepartment(updatedEmployee.getDepartment());
                    return employeeRepository.save(employee);
                })
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with id " + id));
    }

    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);
    }

    public List<EmployeeDTO> searchEmployees(String name, String department, String designation) {
        Specification<Employee> spec = EmployeeSpecifications.withFilters(name, department, designation);
        return employeeRepository.findAll(spec).stream().map(employeeMapper::toDto).collect(Collectors.toList());
    }

    public Employee getEmployeeByAuthentication(Authentication authentication) throws IllegalArgumentException
    {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        Map<String, Object> claims = jwtToken.getToken().getClaims();
        String userEmail = (String) claims.get("email");
        Employee employeeAu = employeeRepository.findByEmail(userEmail)
                .orElseThrow(()-> new IllegalArgumentException("Employee not found with Email: " + userEmail));
        return employeeAu;
    }

    public List<Employee> getEmployeesWithoutProjects() {
        return employeeRepository.findEmployeesWithoutProjects();
    }

    public List<Employee> getEmployeesByProject(int projectId) {
        return employeeRepository.findEmployeesByProjectId(projectId);
    }
}
