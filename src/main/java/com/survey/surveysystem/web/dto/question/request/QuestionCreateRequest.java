package com.survey.surveysystem.web.dto.question.request;

import com.survey.surveysystem.domain.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionCreateRequest(
        @NotBlank
        String content,
        @NotNull
        Question.QuestionType type,
        @NotNull
        Boolean isRequired,
        @NotNull
        Boolean isSatisfaction
) {
}
