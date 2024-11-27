package org.airtribe.employeetracking.service;

import org.airtribe.employeetracking.dto.ManagerDTO;
import org.airtribe.employeetracking.entity.Manager;
import org.airtribe.employeetracking.mapper.ManagerMapper;
import org.airtribe.employeetracking.repository.ManagerRepository;
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

class ManagerServiceTest {

    @InjectMocks
    private ManagerService managerService;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private ManagerMapper managerMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllManagers_WhenNoManagersExist() {
        when(managerRepository.findAll()).thenReturn(null);

        List<ManagerDTO> result = managerService.getAllManagers();

        assertNull(result);
        verify(managerRepository, times(1)).findAll();
        verify(managerMapper, never()).toDto(any(Manager.class));
    }

    @Test
    void testGetManagerById_WhenManagerExists() {
        Manager manager = new Manager();
        manager.setUserId(1);

        when(managerRepository.findById(1)).thenReturn(Optional.of(manager));

        Optional<Manager> result = managerService.getManagerById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getUserId());
        verify(managerRepository, times(1)).findById(1);
    }

    @Test
    void testGetManagerById_WhenManagerDoesNotExist() {
        when(managerRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Manager> result = managerService.getManagerById(1);

        assertFalse(result.isPresent());
        verify(managerRepository, times(1)).findById(1);
    }

    @Test
    void testCreateManager() {
        ManagerDTO managerDTO = new ManagerDTO();
        Manager manager = new Manager();

        when(managerMapper.toEntity(managerDTO)).thenReturn(manager);
        when(managerRepository.save(manager)).thenReturn(manager);

        Manager result = managerService.createManager(managerDTO);

        assertNotNull(result);
        verify(managerMapper, times(1)).toEntity(managerDTO);
        verify(managerRepository, times(1)).save(manager);
    }

    @Test
    void testUpdateManager_WhenManagerExists() {
        Manager existingManager = new Manager();
        existingManager.setUserId(1);
        existingManager.setName("Old Name");

        Manager updatedManager = new Manager();
        updatedManager.setName("New Name");

        when(managerRepository.findById(1)).thenReturn(Optional.of(existingManager));
        when(managerRepository.save(any(Manager.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Manager result = managerService.updateManager(1, updatedManager);

        assertEquals("New Name", result.getName());
        verify(managerRepository, times(1)).findById(1);
        verify(managerRepository, times(1)).save(existingManager);
    }

    @Test
    void testUpdateManager_WhenManagerDoesNotExist() {
        Manager updatedManager = new Manager();

        when(managerRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                managerService.updateManager(1, updatedManager));

        assertEquals("Manager not found with id 1", exception.getMessage());
        verify(managerRepository, times(1)).findById(1);
        verify(managerRepository, never()).save(any(Manager.class));
    }

    @Test
    void testDeleteManager() {
        doNothing().when(managerRepository).deleteById(1);

        managerService.deleteManager(1);

        verify(managerRepository, times(1)).deleteById(1);
    }
}
