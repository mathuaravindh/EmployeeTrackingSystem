package org.airtribe.employeetracking.service;

import org.airtribe.employeetracking.entity.User;
import org.airtribe.employeetracking.entity.enums.Roles;
import org.airtribe.employeetracking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoleByEmail_UserExists() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setRole(Roles.ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        String role = userService.getRoleByEmail(email);

        // Assert
        assertNotNull(role);
        assertEquals("ADMIN", role);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetRoleByEmail_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getRoleByEmail(email)
        );

        assertEquals("User not found with Email: " + email, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }
}
