package com.survey.surveysystem.web.dto.question.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionUpdateRequest(
        @NotBlank
        String content,
        @NotNull
        Boolean isRequired
) {
}
