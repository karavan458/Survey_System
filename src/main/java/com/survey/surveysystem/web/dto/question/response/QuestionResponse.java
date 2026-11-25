package com.survey.surveysystem.web.dto.question.response;

import com.survey.surveysystem.domain.entity.Question;

public record QuestionResponse(
        Long id,
        Long surveyId,
        int questionNumber,
        String content,
        Question.QuestionType type,
        boolean isRequired,
        boolean isSatisfaction
) {
    public static QuestionResponse from(Question question, int questionNumber) {
        return new QuestionResponse(
                question.getId(),
                question.getSurvey().getId(),
                questionNumber,
                question.getContent(),
                question.getType(),
                question.isRequired(),
                question.isSatisfaction()
        );
    }
}
