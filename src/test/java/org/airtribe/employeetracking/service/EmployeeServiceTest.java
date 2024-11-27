package org.airtribe.employeetracking.service;

import org.airtribe.employeetracking.dto.EmployeeDTO;
import org.airtribe.employeetracking.entity.Department;
import org.airtribe.employeetracking.entity.Employee;
import org.airtribe.employeetracking.mapper.EmployeeMapper;
import org.airtribe.employeetracking.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private JwtAuthenticationToken jwtToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Employee employee = new Employee();
        when(employeeMapper.toEntity(employeeDTO)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.addEmployee(employeeDTO);

        assertNotNull(result);
        verify(employeeMapper, times(1)).toEntity(employeeDTO);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(new EmployeeDTO());

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
        verify(employeeMapper, times(2)).toDto(any(Employee.class));
    }

    @Test
    void testGetEmployeeById_Found() {
        Employee employee = new Employee();
        employee.setUserId(1);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee)).thenReturn(new EmployeeDTO());

        Optional<EmployeeDTO> result = employeeService.getEmployeeById(1);

        assertTrue(result.isPresent());
        verify(employeeRepository, times(1)).findById(1);
        verify(employeeMapper, times(1)).toDto(employee);
    }


    @Test
    void testUpdateEmployee_Found() {
        Employee existingEmployee = new Employee();
        existingEmployee.setUserId(1);
        existingEmployee.setName("Old Name");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("New Name");

        when(employeeRepository.findById(1)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = employeeService.updateEmployee(1, updatedEmployee);

        assertEquals("New Name", result.getName());
        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).save(existingEmployee);
    }

    @Test
    void testUpdateEmployee_NotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                employeeService.updateEmployee(1, new Employee()));

        assertEquals("Employee not found with id 1", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(1);

        employeeService.deleteEmployee(1);

        verify(employeeRepository, times(1)).deleteById(1);
    }

    @Test
    void testSearchEmployees() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findAll(any(Specification.class))).thenReturn(employees);
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(new EmployeeDTO());

        List<EmployeeDTO> result = employeeService.searchEmployees("John", "IT", "Developer");

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll(any(Specification.class));
        verify(employeeMapper, times(2)).toDto(any(Employee.class));
    }


    @Test
    void testGetEmployeesWithoutProjects() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findEmployeesWithoutProjects()).thenReturn(employees);

        List<Employee> result = employeeService.getEmployeesWithoutProjects();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findEmployeesWithoutProjects();
    }

    @Test
    void testGetEmployeesByProject() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findEmployeesByProjectId(1)).thenReturn(employees);

        List<Employee> result = employeeService.getEmployeesByProject(1);

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findEmployeesByProjectId(1);
    }
}
