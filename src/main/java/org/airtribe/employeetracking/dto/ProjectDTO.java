package org.airtribe.employeetracking.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.airtribe.employeetracking.entity.enums.ProjectStatus;

import java.util.Date;

@Getter
@Setter
public class ProjectDTO {
    private String projectName;

    private Date startDate;

    private Date endDate;

    private String projectStatus;

    private String departmentName;

    private double budget;
}
