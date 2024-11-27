package org.airtribe.employeetracking.repository.dataloader;

import org.airtribe.employeetracking.entity.Admin;
import org.airtribe.employeetracking.entity.enums.Roles;
import org.airtribe.employeetracking.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminDataLoader implements ApplicationRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.email}")
    private String adminEmail;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (adminRepository.findByEmail(adminEmail).isEmpty()) {
            Admin admin = new Admin();
            admin.setName(adminUsername);
            admin.setEmail(adminEmail);
            admin.setRole(Roles.ADMIN);

            adminRepository.save(admin);
            System.out.println("Saved admin user: " + adminUsername);
        } else {
            System.out.println("Admin user already exists with email: " + adminEmail);
        }
    }
}
