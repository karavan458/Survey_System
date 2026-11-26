package com.survey.surveysystem.web.dto.survey.response;

import com.survey.surveysystem.domain.entity.Survey;
import com.survey.surveysystem.web.dto.question.response.QuestionDetailResponse;

import java.time.LocalDate;
import java.util.List;

public record SurveyDetailResponse(
        Long id,
        String title,
        String description,
        Survey.SurveyStatus status,
        boolean isAnonymous,
        LocalDate startDate,
        LocalDate endDate,
        Long creatorId,
        String creatorName,
        List<QuestionDetailResponse> questions
) {
    public static SurveyDetailResponse from(Survey survey, List<QuestionDetailResponse> questions) {
        return new SurveyDetailResponse(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getStatus(),
                survey.isAnonymous(),
                survey.getStartDate(),
                survey.getEndDate(),
                survey.getCreator().getId(),
                survey.getCreator().getName(),
                questions
        );
    }
}
