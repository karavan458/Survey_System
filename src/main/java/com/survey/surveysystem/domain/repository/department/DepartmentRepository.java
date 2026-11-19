package com.survey.surveysystem.domain.repository.department;

import com.survey.surveysystem.domain.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
