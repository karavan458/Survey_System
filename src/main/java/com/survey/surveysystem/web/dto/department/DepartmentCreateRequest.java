package com.survey.surveysystem.web.dto.department;

import jakarta.validation.constraints.NotBlank;

public record DepartmentCreateRequest(
        @NotBlank
        String name,
        String description
) {
}
