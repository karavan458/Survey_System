package com.survey.surveysystem.web.dto.department;

import jakarta.validation.constraints.NotBlank;

public record DepartmentUpdateRequest(
        @NotBlank
        String name,
        String description
) {
}
