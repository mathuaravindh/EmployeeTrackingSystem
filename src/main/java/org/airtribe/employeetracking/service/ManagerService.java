package org.airtribe.employeetracking.service;

import org.airtribe.employeetracking.dto.ManagerDTO;
import org.airtribe.employeetracking.entity.Manager;
import org.airtribe.employeetracking.entity.User;
import org.airtribe.employeetracking.mapper.ManagerMapper;
import org.airtribe.employeetracking.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerMapper managerMapper;

    // Get all managers
    public List<ManagerDTO> getAllManagers() {
        if(managerRepository.findAll() != null)
        {
            return managerRepository.findAll().stream().map(managerMapper::toDto).collect(Collectors.toList());
        }
        return null;
    }

    // Get a specific manager by ID
    public Optional<Manager> getManagerById(int id) {
        return managerRepository.findById(id);
    }

    // Create a new manager
    public Manager createManager(ManagerDTO managerDTO) {
        return managerRepository.save(managerMapper.toEntity(managerDTO));
    }

    // Update an existing manager
    public Manager updateManager(int id, Manager updatedManager) {
        return managerRepository.findById(id)
                .map(manager -> {
                    manager.setName(updatedManager.getName());
                    manager.setEmail(updatedManager.getEmail());
                    manager.setRole(updatedManager.getRole());
                    manager.setDepartment(updatedManager.getDepartment());
                    return managerRepository.save(manager);
                })
                .orElseThrow(() -> new IllegalArgumentException("Manager not found with id " + id));
    }

    // Delete a manager by ID
    public void deleteManager(int id) {
        managerRepository.deleteById(id);
    }

    public Manager getManagerByAuthentication(Authentication authentication) throws IllegalArgumentException
    {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        Map<String, Object> claims = jwtToken.getToken().getClaims();
        String userEmail = (String) claims.get("email");
        Manager managerAu =  managerRepository.findByEmail(userEmail)
                .orElseThrow(()-> new IllegalArgumentException("Manager not found with Email: " + userEmail));
        return managerAu;
    }
}
