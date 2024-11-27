package org.airtribe.employeetracking.controller;

import org.airtribe.employeetracking.dto.ManagerDTO;
import org.airtribe.employeetracking.entity.Manager;
import org.airtribe.employeetracking.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    // Get all managers
    @GetMapping("/managers")
    @PreAuthorize("@securityService.hasAccessToAdminEndpoint(authentication)")
    public List<ManagerDTO> getAllManagers() {
        return managerService.getAllManagers();
    }

    // Get a manager by ID
    @GetMapping("/managers/{id}")
    @PreAuthorize("@securityService.hasAccessToAdminEndpoint(authentication)")
    public ResponseEntity<Manager> getManagerById(@PathVariable int id) {
        return managerService.getManagerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new manager
    @PostMapping("/managers")
    @PreAuthorize("@securityService.hasAccessToAdminEndpoint(authentication)")
    public Manager createManager(@RequestBody ManagerDTO managerDTO) {
        return managerService.createManager(managerDTO);
    }

    // Update an existing manager
    @PutMapping("/managers/{id}")
    @PreAuthorize("@securityService.hasAccessToAdminEndpoint(authentication)")
    public ResponseEntity<Manager> updateManager(
            @PathVariable int id,
            @RequestBody Manager updatedManager
    ) {
        try {
            return ResponseEntity.ok(managerService.updateManager(id, updatedManager));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a manager
    @DeleteMapping("/managers/{id}")
    @PreAuthorize("@securityService.hasAccessToAdminEndpoint(authentication)")
    public ResponseEntity<Void> deleteManager(@PathVariable int id) {
        managerService.deleteManager(id);
        return ResponseEntity.noContent().build();
    }
}
