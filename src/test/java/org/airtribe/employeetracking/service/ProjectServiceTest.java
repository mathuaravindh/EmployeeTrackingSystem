package org.airtribe.employeetracking.service;

import org.airtribe.employeetracking.dto.ProjectDTO;
import org.airtribe.employeetracking.entity.Project;
import org.airtribe.employeetracking.mapper.ProjectMapper;
import org.airtribe.employeetracking.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProjects_WhenNoProjectsExist() {
        when(projectRepository.findAll()).thenReturn(null);

        List<ProjectDTO> result = projectService.getAllProjects();

        assertNull(result);
        verify(projectRepository, times(1)).findAll();
        verify(projectMapper, never()).toDto(any(Project.class));
    }

    @Test
    void testGetProjectById_WhenProjectExists() {
        Project project = new Project();
        project.setProjectId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getProjectId());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void testGetProjectById_WhenProjectDoesNotExist() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Project> result = projectService.getProjectById(1);

        assertFalse(result.isPresent());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void testCreateProject() {
        ProjectDTO projectDTO = new ProjectDTO();
        Project project = new Project();

        when(projectMapper.toEntity(projectDTO)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.createProject(projectDTO);

        assertNotNull(result);
        verify(projectMapper, times(1)).toEntity(projectDTO);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testUpdateProject_WhenProjectExists() {
        Project existingProject = new Project();
        existingProject.setProjectId(1);
        existingProject.setProjectName("Old Name");

        Project updatedProject = new Project();
        updatedProject.setProjectName("New Name");

        when(projectRepository.findById(1)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project result = projectService.updateProject(1, updatedProject);

        assertEquals("New Name", result.getProjectName());
        verify(projectRepository, times(1)).findById(1);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    void testUpdateProject_WhenProjectDoesNotExist() {
        Project updatedProject = new Project();

        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                projectService.updateProject(1, updatedProject));

        assertEquals("Project not found with id 1", exception.getMessage());
        verify(projectRepository, times(1)).findById(1);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void testDeleteProject() {
        doNothing().when(projectRepository).deleteById(1);

        projectService.deleteProject(1);

        verify(projectRepository, times(1)).deleteById(1);
    }
}
