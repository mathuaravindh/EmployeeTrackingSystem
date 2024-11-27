package org.airtribe.employeetracking.repository;

import org.airtribe.employeetracking.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findByProjectName(String projectName);

    @Query("SELECT SUM(p.budget) FROM Project p WHERE p.department.deptId = :deptId")
    Double getTotalBudgetByDepartment(@Param("deptId") int deptId);
}
