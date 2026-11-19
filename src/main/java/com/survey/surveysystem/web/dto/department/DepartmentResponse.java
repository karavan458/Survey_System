package com.survey.surveysystem.web.dto.department;

import com.survey.surveysystem.domain.entity.Department;

public record DepartmentResponse(
        Long id,
        String name,
        String description
) {
    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription()
        );
    }
}
