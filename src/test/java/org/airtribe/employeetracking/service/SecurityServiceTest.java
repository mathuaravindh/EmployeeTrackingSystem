package org.airtribe.employeetracking.service;

import org.airtribe.employeetracking.dto.EmployeeDTO;
import org.airtribe.employeetracking.entity.Department;
import org.airtribe.employeetracking.entity.Employee;
import org.airtribe.employeetracking.entity.Manager;
import org.airtribe.employeetracking.entity.User;
import org.airtribe.employeetracking.entity.enums.Roles;
import org.airtribe.employeetracking.exception.customexception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityServiceTest {

    @InjectMocks
    private SecurityService securityService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ManagerService managerService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testHasAccessToManagerEndpoint_InvalidAuthentication() throws UserNotFoundException {
        when(authentication.isAuthenticated()).thenReturn(false);

        boolean result = securityService.hasAccessToManagerEndpoint(authentication);

        assertFalse(result);
        verifyNoInteractions(userService);
    }

    @Test
    void testHasAccessToEmployeeEndpoint_InvalidUser() throws UserNotFoundException {
        when(authentication.isAuthenticated()).thenReturn(false);

        boolean result = securityService.hasAccessToEmployeeEndpoint(authentication, 100);

        assertFalse(result);
        verifyNoInteractions(employeeService);
    }
}
