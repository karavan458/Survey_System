package com.survey.surveysystem.web.dto.survey.request;

import com.survey.surveysystem.domain.entity.Survey;
import jakarta.validation.constraints.NotNull;

public record SurveyStatusChangeRequest(
        @NotNull
        Survey.SurveyStatus status
) {
}
