package org.airtribe.employeetracking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.airtribe.employeetracking.entity.enums.Roles;

import javax.management.relation.Role;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int userId;

    @NotBlank(message = "Name is required")
    @Size(min = 4, max = 20, message = "Name must be between 4 and 20 characters")
    String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email;

    @Enumerated(EnumType.STRING)
    Roles role;
}
