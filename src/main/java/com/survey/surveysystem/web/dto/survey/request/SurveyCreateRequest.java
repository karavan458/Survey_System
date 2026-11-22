package com.survey.surveysystem.web.dto.survey.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SurveyCreateRequest(
        @NotBlank
        String title,
        String description,
        @NotNull
        Boolean isAnonymous,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate,
        @NotNull
        Long creatorId
) {
}
