package org.airtribe.employeetracking.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeDTO {

    private int userId;
    private String name;
    private String email;
    private String designation;
    private String departmentName;
    private List<String> projectNames;

}
