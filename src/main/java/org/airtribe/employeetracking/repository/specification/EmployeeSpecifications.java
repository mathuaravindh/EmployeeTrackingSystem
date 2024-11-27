package org.airtribe.employeetracking.repository.specification;

import org.airtribe.employeetracking.entity.Employee;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecifications {

    public static Specification<Employee> withFilters(String name, String deptName, String designation) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (deptName != null && !deptName.isEmpty()) {
                predicates.add(cb.equal(root.join("department").get("deptName"), deptName));
            }
            if (designation != null && !designation.isEmpty()) {
                predicates.add(cb.equal(root.get("designation"), designation));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
