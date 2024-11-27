package org.airtribe.employeetracking.service;

import org.airtribe.employeetracking.dto.EmployeeDTO;
import org.airtribe.employeetracking.entity.Employee;
import org.airtribe.employeetracking.entity.Manager;
import org.airtribe.employeetracking.entity.User;
import org.airtribe.employeetracking.exception.customexception.UserNotFoundException;
import org.airtribe.employeetracking.service.EmployeeService;
import org.airtribe.employeetracking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    UserService userService;


    public boolean hasAccessToAdminEndpoint(Authentication authentication) throws UserNotFoundException {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        if (!(authentication instanceof JwtAuthenticationToken)){
            return false;
        }
        User user = userService.getUserByAuthentication(authentication);
        return "ADMIN".equals(user.getRole().name());
    }

    public boolean hasAccessToManagerEndpoint(Authentication authentication) throws UserNotFoundException {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        if (!(authentication instanceof JwtAuthenticationToken)){
            return false;
        }
        User user = userService.getUserByAuthentication(authentication);
        String role = user.getRole().name();
        return ("ADMIN".equals(role) || "MANAGER".equals(role));
    }

    public boolean hasAccessToManagerEndpoint(Authentication authentication, EmployeeDTO employeeDTO) throws UserNotFoundException {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        if (!(authentication instanceof JwtAuthenticationToken)){
            return false;
        }
        Manager manager = managerService.getManagerByAuthentication(authentication);
        String role = manager.getRole().name();

        if("MANAGER".equals(role)){
            return (manager.getDepartment().getDeptName()).equals(employeeDTO.getDepartmentName());
        }
        return "ADMIN".equals(role);
    }

    public boolean hasAccessToManagerEndpoint(Authentication authentication, int id) throws UserNotFoundException {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        if (!(authentication instanceof JwtAuthenticationToken)){
            return false;
        }
        Manager manager = managerService.getManagerByAuthentication(authentication);
        String role = manager.getRole().name();

        if("MANAGER".equals(role)){
            return (manager.getDepartment().getDeptName()).equals(employeeService.getEmployeeById(id).get().getDepartmentName());
        }
        return "ADMIN".equals(role);
    }


    public boolean hasAccessToEmployeeEndpoint(Authentication authentication, int id) throws UserNotFoundException {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        if (!(authentication instanceof JwtAuthenticationToken)){
            return false;
        }
        Employee employee = employeeService.getEmployeeByAuthentication(authentication);
        String role = employee.getRole().name();

        if("EMPLOYEE".equals(role)){
            return employee.getUserId() == id;
        }
        if("MANAGER".equals(role)){
            return (employee.getDepartment().getDeptName()).equals(employeeService.getEmployeeById(id).get().getDepartmentName());
        }
        return "ADMIN".equals(role);
    }

}