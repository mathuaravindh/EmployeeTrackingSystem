package org.airtribe.employeetracking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.airtribe.employeetracking.entity.enums.ProjectStatus;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;

    private String projectName;

    private Date startDate;

    private Date endDate;

    private double budget;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @ManyToOne
    @JoinColumn(name="deptId")
    @JsonBackReference
    private Department department;

    @ManyToMany(mappedBy = "projectList")
    @JsonIgnore
    private List<Employee> employeeList;
}
