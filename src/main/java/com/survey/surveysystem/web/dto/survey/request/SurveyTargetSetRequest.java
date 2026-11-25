package com.survey.surveysystem.web.dto.survey.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SurveyTargetSetRequest(
        @NotEmpty
        List<Long> departmentIds
) {
}
