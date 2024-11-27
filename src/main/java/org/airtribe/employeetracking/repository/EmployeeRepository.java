package org.airtribe.employeetracking.repository;

import org.airtribe.employeetracking.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {
    public Optional<Employee> findByEmail(String email);

    @Query("SELECT e " +
            "FROM Employee e " +
            "LEFT JOIN e.projectList p " +
            "WHERE p IS NULL")
    List<Employee> findEmployeesWithoutProjects();

    @Query("SELECT e FROM Employee e JOIN e.projectList p WHERE p.projectId = :projectId")
    List<Employee> findEmployeesByProjectId(@Param("projectId") int projectId);
}
