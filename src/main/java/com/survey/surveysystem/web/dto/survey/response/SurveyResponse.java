package com.survey.surveysystem.web.dto.survey.response;

import com.survey.surveysystem.domain.entity.Survey;

import java.time.LocalDate;

public record SurveyResponse(
        Long id,
        String title,
        String description,
        Survey.SurveyStatus status,
        boolean isAnonymous,
        LocalDate startDate,
        LocalDate endDate,
        Long creatorId,
        String creatorName
) {
    public static SurveyResponse from(Survey survey) {
        return new SurveyResponse(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getStatus(),
                survey.isAnonymous(),
                survey.getStartDate(),
                survey.getEndDate(),
                survey.getCreator().getId(),
                survey.getCreator().getName()
        );
    }
}
