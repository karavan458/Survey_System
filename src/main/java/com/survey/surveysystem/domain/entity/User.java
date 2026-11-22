package com.survey.surveysystem.domain.entity;

import com.survey.surveysystem.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Getter
@Entity
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    private String position;
    private LocalDate hireDate;

    private User(String email, String encodedPassword, String name, Role role, Department department, String position, LocalDate hireDate) {
        this.email = email;
        this.password = encodedPassword;
        this.name = name;
        this.role = role;
        this.department = department;
        this.position = position;
        this.hireDate = hireDate;
    }

    public static User create(String email, String encodedPassword, String name, Role role, Department department, String position, LocalDate hireDate) {
        return new User(email, encodedPassword, name, role, department, position, hireDate);
    }

    public void changeRole(Role role) {
        this.role = role;
    }

    public void changeDepartment(Department department) {
        this.department = department;
    }

    public void changePosition(String position) {
        this.position = position;
    }

    public enum Role {
        ADMIN, MANAGER, EMPLOYEE
    }
}
