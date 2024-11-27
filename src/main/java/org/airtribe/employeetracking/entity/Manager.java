package org.airtribe.employeetracking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Manager extends User {

    @OneToOne
    @JoinColumn(name = "deptId")
    @JsonBackReference
    private Department department;
}
