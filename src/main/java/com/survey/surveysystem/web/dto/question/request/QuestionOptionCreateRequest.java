package com.survey.surveysystem.web.dto.question.request;

import jakarta.validation.constraints.NotBlank;

public record QuestionOptionCreateRequest(
        @NotBlank
        String content
) {
}
