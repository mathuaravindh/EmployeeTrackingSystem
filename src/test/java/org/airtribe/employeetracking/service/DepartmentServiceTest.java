package org.airtribe.employeetracking.service;

import org.airtribe.employeetracking.dto.ProjectDTO;
import org.airtribe.employeetracking.entity.Department;
import org.airtribe.employeetracking.entity.Project;
import org.airtribe.employeetracking.mapper.ProjectMapper;
import org.airtribe.employeetracking.repository.DepartmentRepository;
import org.airtribe.employeetracking.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceTest {

    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDepartments() {
        List<Department> departments = Arrays.asList(
                new Department(),
                new Department()
        );

        when(departmentRepository.findAll()).thenReturn(departments);

        List<Department> result = departmentService.getAllDepartments();

        assertEquals(2, result.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testGetDepartmentById_Found() {
        Department department = new Department();
        department.setDeptId(1);

        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));

        Optional<Department> result = departmentService.getDepartmentById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getDeptId());
        verify(departmentRepository, times(1)).findById(1);
    }

    @Test
    void testGetDepartmentById_NotFound() {
        when(departmentRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Department> result = departmentService.getDepartmentById(1);

        assertFalse(result.isPresent());
        verify(departmentRepository, times(1)).findById(1);
    }

    @Test
    void testCreateDepartment() {
        Department department = new Department();
        when(departmentRepository.save(department)).thenReturn(department);

        Department result = departmentService.createDepartment(department);

        assertNotNull(result);
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void testUpdateDepartment_Found() {
        Department existingDepartment = new Department();
        existingDepartment.setDeptId(1);
        existingDepartment.setDeptName("HR");

        Department updatedDepartment = new Department();
        updatedDepartment.setDeptName("IT");

        when(departmentRepository.findById(1)).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Department result = departmentService.updateDepartment(1, updatedDepartment);

        assertEquals("IT", result.getDeptName());
        verify(departmentRepository, times(1)).findById(1);
        verify(departmentRepository, times(1)).save(existingDepartment);
    }

    @Test
    void testUpdateDepartment_NotFound() {
        when(departmentRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                departmentService.updateDepartment(1, new Department()));

        assertEquals("Department not found with id 1", exception.getMessage());
        verify(departmentRepository, times(1)).findById(1);
        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    void testDeleteDepartment() {
        doNothing().when(departmentRepository).deleteById(1);

        departmentService.deleteDepartment(1);

        verify(departmentRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetDepartmentProjectsById_NotFound() {
        when(departmentRepository.findById(1)).thenReturn(Optional.empty());

        List<ProjectDTO> result = departmentService.getDepartmentProjectsById(1);

        assertNull(result);
        verify(departmentRepository, times(1)).findById(1);
        verify(projectMapper, never()).toDto(any(Project.class));
    }

    @Test
    void testGetTotalBudgetForDepartment() {
        when(projectRepository.getTotalBudgetByDepartment(1)).thenReturn(100000.0);

        Double result = departmentService.getTotalBudgetForDepartment(1);

        assertEquals(100000.0, result);
        verify(projectRepository, times(1)).getTotalBudgetByDepartment(1);
    }
}
