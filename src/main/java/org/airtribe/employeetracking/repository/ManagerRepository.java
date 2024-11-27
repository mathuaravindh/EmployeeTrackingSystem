package org.airtribe.employeetracking.repository;

import org.airtribe.employeetracking.entity.Employee;
import org.airtribe.employeetracking.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    public Optional<Manager> findByEmail(String email);
}
